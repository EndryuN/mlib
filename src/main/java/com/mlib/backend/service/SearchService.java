// src/main/java/com/mlib/backend/service/SearchService.java
package com.mlib.backend.service;

import com.mlib.backend.dto.AliasDto;
import com.mlib.backend.dto.ArtistDto;
import com.mlib.backend.dto.SongDto;
import com.mlib.backend.model.Album;
import com.mlib.backend.model.Artist;
import com.mlib.backend.model.ArtistAlias;
import com.mlib.backend.model.Song;
import com.mlib.backend.repository.AlbumRepository;
import com.mlib.backend.repository.ArtistRepository;
import com.mlib.backend.repository.ArtistAliasRepository;
import com.mlib.backend.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistAliasRepository artistAliasRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;


    /**
     * Search artists by name OR alias (case-insensitive)
     */
    public List<ArtistDto> searchArtists(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String trimmedQuery = query.trim();

        // Step 1: Find all artists matching name OR alias
        List<Artist> allArtists = artistRepository.searchArtists(trimmedQuery);

        if (allArtists.isEmpty()) {
            return Collections.emptyList();
        }

        // Step 2: Extract all unique artistIds to fetch their aliases
        List<Integer> artistIds = allArtists.stream()
                .map(Artist::getArtistId)
                .toList();

        // Step 3: Fetch all aliases for these artists
        List<ArtistAlias> allAliases = artistIds.isEmpty() ?
                Collections.emptyList() :
                artistAliasRepository.findByArtistIdIn(artistIds);

        // Group aliases by artistId
        Map<Integer, List<ArtistAlias>> aliasMap = allAliases.stream()
                .collect(Collectors.groupingBy(ArtistAlias::getArtistId));

        // Step 4: Group artists by artistUri
        Map<String, List<Artist>> artistsByUri = allArtists.stream()
                .filter(a -> a.getArtistUri() != null)
                .collect(Collectors.groupingBy(Artist::getArtistUri));

        // Step 5: Build one ArtistDto per artistUri
        return artistsByUri.entrySet().stream()
                .map(entry -> {
                    String artistUri = entry.getKey();
                    List<Artist> duplicates = entry.getValue();

                    // Pick the "best" representative artist (e.g. verified first)
                    Artist representative = duplicates.stream()
                            .max(Comparator.comparing(a -> a.getVerifiedCheck() != null && a.getVerifiedCheck()))
                            .orElse(duplicates.get(0));

                    // Gather all unique aliases across all duplicates
                    Set<ArtistAlias> combinedAliases = duplicates.stream()
                            .flatMap(d -> aliasMap.getOrDefault(d.getArtistId(), Collections.emptyList()).stream())
                            .collect(Collectors.toCollection(LinkedHashSet::new)); // preserve order

                    // Convert to DTO
                    ArtistDto dto = new ArtistDto();
                    dto.setArtistId(representative.getArtistId());  // representative ID
                    dto.setArtistName(representative.getArtistName());
                    dto.setArtistUri(artistUri);
                    dto.setVerifiedCheck(representative.getVerifiedCheck());

                    // Attach all aliases from all duplicates
                    List<AliasDto> aliasDtos = combinedAliases.stream()
                            .map(alias -> {
                                AliasDto ad = new AliasDto();
                                ad.setAliasId(alias.getAliasId());
                                ad.setAliasName(alias.getAliasName());
                                return ad;
                            })
                            .collect(Collectors.toList());

                    dto.setAliases(aliasDtos);
                    return dto;
                })
                .sorted(Comparator.comparing(dto -> dto.getArtistName())) // optional: sort by name
                .collect(Collectors.toList());
    }

    /**
     * Search songs by name or featured artists
     */
    public List<SongDto> searchSongs(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<Song> songs = songRepository.searchSongs(query.trim());
        return convertToSongDtos(songs);
    }

    /**
     * Get songs by artist ID
     */
    public List<SongDto> getSongsByArtistId(Integer artistId) {
        List<Song> songs = songRepository.findByArtistId(artistId);
        return convertToSongDtos(songs);
    }

    /**
     * Filter songs by language
     */
    public List<SongDto> filterByLanguage(String language) {
        List<Song> songs = songRepository.findByLanguage(language);
        return convertToSongDtos(songs);
    }

    /**
     * Filter songs by release status
     */
    public List<SongDto> filterByReleaseStatus(List<String> statuses) {
        List<Song> songs = songRepository.findByReleaseStatusIn(statuses);
        return convertToSongDtos(songs);
    }

    /**
     * Convert Song entities to SongDto with joined artist/album names
     */
    private List<SongDto> convertToSongDtos(List<Song> songs) {
        // Step 1: Fetch artist names
        Map<Integer, String> artistNameMap = new HashMap<>();
        if (!songs.isEmpty()) {
            Set<Integer> artistIds = songs.stream()
                    .map(Song::getArtistId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (!artistIds.isEmpty()) {
                List<Artist> artists = artistRepository.findByIdIn(new ArrayList<>(artistIds));
                artistNameMap.putAll(artists.stream()
                        .collect(Collectors.toMap(Artist::getArtistId, Artist::getArtistName)));
            }
        }

        // Step 2: Fetch album names
        Map<Integer, String> albumNameMap = new HashMap<>();
        if (!songs.isEmpty()) {
            Set<Integer> albumIds = songs.stream()
                    .map(Song::getAlbumId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (!albumIds.isEmpty()) {
                List<Album> albums = albumRepository.findByIdIn(new ArrayList<>(albumIds));
                albumNameMap.putAll(albums.stream()
                        .collect(Collectors.toMap(Album::getAlbumId, Album::getAlbumName)));
            }
        }

        // Step 3: Convert songs to SongDto with joined names
        return songs.stream()
                .map(song -> {
                    SongDto dto = new SongDto();
                    dto.setSongId(song.getSongId());
                    dto.setSongName(song.getSongName());
                    dto.setArtistId(song.getArtistId());
                    dto.setAlbumId(song.getAlbumId());
                    dto.setGenre(song.getGenre());
                    dto.setDurationMs(song.getDurationMs());
                    dto.setTrackUri(song.getTrackUri());
                    dto.setIsSingle(song.getIsSingle());
                    dto.setFeaturedArtists(song.getFeaturedArtists());
                    dto.setTrackOrder(song.getTrackOrder());
                    dto.setLanguage(song.getLanguage());
                    dto.setReleaseStatus(song.getReleaseStatus());

                    // Use the maps to set artistName and albumName
                    dto.setArtistName(artistNameMap.getOrDefault(song.getArtistId(), "Unknown Artist"));
                    dto.setAlbumName(albumNameMap.getOrDefault(song.getAlbumId(), "Unknown Album"));

                    return dto;
                })
                .collect(Collectors.toList());
    }
}