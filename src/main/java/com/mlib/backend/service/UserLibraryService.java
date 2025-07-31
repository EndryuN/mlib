package com.mlib.backend.service;

import com.mlib.backend.dto.*;
import com.mlib.backend.exception.*;
import com.mlib.backend.model.*;
import com.mlib.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserLibraryService {

    @Autowired
    private UserFollowedArtistRepository userFollowedArtistRepository;

    @Autowired
    private UserSavedSongRepository userSavedSongRepository;

    @Autowired
    private UserSavedAlbumRepository userSavedAlbumRepository;

    @Autowired
    private UserSavedPlaylistRepository userSavedPlaylistRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistAliasRepository artistAliasRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    // ======================
    // ARTIST FOLLOWING
    // ======================

    public void followArtist(Long userId, String artistUri) {
        if (artistUri == null || artistUri.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist URI is required");
        }
        artistUri = artistUri.trim();

        // Validate that at least one artist with this URI exists
        if (!artistRepository.existsByArtistUri(artistUri)) {
            throw new ArtistNotFoundException("No artist found with URI: " + artistUri);
        }

        UserFollowedArtistId id = new UserFollowedArtistId(userId, artistUri);

        if (!userFollowedArtistRepository.existsById(id)) {
            UserFollowedArtist followed = new UserFollowedArtist();
            followed.setId(id);
            followed.setFollowedAt(LocalDateTime.now());
            userFollowedArtistRepository.save(followed);
        }else {
            throw new DuplicateEntryException("Artist already followed by user");
        }
    }

    public void unfollowArtist(Long userId, String artistUri) {
        if (artistUri == null || artistUri.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist URI is required");
        }
        artistUri = artistUri.trim();

        UserFollowedArtistId id = new UserFollowedArtistId(userId, artistUri);
        userFollowedArtistRepository.deleteById(id);
    }

    public List<ArtistDto> getUserFollowedArtists(Long userId) {
        List<String> artistUris = userFollowedArtistRepository.findArtistUrisByUserId(userId);
        if (artistUris.isEmpty()) {
            return Collections.emptyList();
        }

        // Step 1: Get all artists with these URIs
        List<Artist> artists = artistRepository.findByArtistUriIn(artistUris);

        // Step 2: Get all aliases for these artists
        List<Integer> artistIds = artists.stream()
                .map(Artist::getArtistId)
                .toList();

        List<ArtistAlias> aliases = artistIds.isEmpty() ?
                Collections.emptyList() :
                artistAliasRepository.findByArtistIdIn(artistIds);

        // Group aliases by artistId
        Map<Integer, List<ArtistAlias>> aliasMap = aliases.stream()
                .collect(Collectors.groupingBy(ArtistAlias::getArtistId));

        // Step 3: Group artists by artistUri
        return artists.stream()
                .filter(a -> a.getArtistUri() != null)
                .collect(Collectors.groupingBy(Artist::getArtistUri))
                .entrySet().stream()
                .map(entry -> {
                    String artistUri = entry.getKey();
                    List<Artist> duplicates = entry.getValue();

                    // Pick the "best" representative (e.g., verified)
                    Artist representative = duplicates.stream()
                            .max(Comparator.comparing(a -> a.getVerifiedCheck() != null && a.getVerifiedCheck()))
                            .orElse(duplicates.get(0));

                    // Gather all unique aliases across duplicates
                    Set<ArtistAlias> combinedAliases = duplicates.stream()
                            .flatMap(d -> aliasMap.getOrDefault(d.getArtistId(), Collections.emptyList()).stream())
                            .collect(Collectors.toCollection(LinkedHashSet::new)); // preserve order

                    // Convert to DTO
                    ArtistDto dto = new ArtistDto();
                    dto.setArtistId(representative.getArtistId());
                    dto.setArtistName(representative.getArtistName());
                    dto.setArtistUri(artistUri);
                    dto.setVerifiedCheck(representative.getVerifiedCheck());

                    // Attach all aliases
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
                .sorted(Comparator.comparing(ArtistDto::getArtistName))
                .collect(Collectors.toList());
    }

    // ======================
    // SONG SAVING
    // ======================

    public void saveSong(Long userId, Integer songId) {
        if (!songRepository.existsById(songId)) {
            throw new SongNotFoundException("Song not found with ID: " + songId);
        }

        UserSavedSongId id = new UserSavedSongId(userId, songId);

        if (!userSavedSongRepository.existsById(id)) {
            UserSavedSong savedSong = new UserSavedSong();
            savedSong.setId(id);
            savedSong.setAddedAt(LocalDateTime.now());
            userSavedSongRepository.save(savedSong);
        }else {
            throw new DuplicateEntryException("Song already saved by user");
        }
    }

    public void unsaveSong(Long userId, Integer songId) {
        UserSavedSongId id = new UserSavedSongId(userId, songId);
        userSavedSongRepository.deleteById(id);
    }

    public List<SongDto> getUserSavedSongs(Long userId) {
        List<UserSavedSong> savedSongs = userSavedSongRepository.findByIdUserIdOrderByAddedAtDesc(userId);
        return savedSongs.stream()
                .map(saved -> convertToSongDto(saved.getSong()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public boolean isSongSaved(Long userId, Integer songId) {
        UserSavedSongId id = new UserSavedSongId(userId, songId);
        return userSavedSongRepository.existsById(id);
    }

    // ======================
    // ALBUM SAVING
    // ======================

    public void saveAlbum(Long userId, Integer albumId) {
        if (!albumRepository.existsById(albumId)) {
            throw new AlbumNotFoundException("Album not found with ID: " + albumId);
        }

        UserSavedAlbumId id = new UserSavedAlbumId(userId, albumId);

        if (!userSavedAlbumRepository.existsById(id)) {
            UserSavedAlbum savedAlbum = new UserSavedAlbum();
            savedAlbum.setId(id);
            savedAlbum.setAddedAt(LocalDateTime.now());
            userSavedAlbumRepository.save(savedAlbum);
        } else {
            throw new DuplicateEntryException("Album already saved by user");
        }
    }

    public void unsaveAlbum(Long userId, Integer albumId) {
        UserSavedAlbumId id = new UserSavedAlbumId(userId, albumId);
        userSavedAlbumRepository.deleteById(id);
    }

    public List<AlbumDto> getUserSavedAlbums(Long userId) {
        List<UserSavedAlbum> savedAlbums = userSavedAlbumRepository.findByIdUserIdOrderByAddedAtDesc(userId);
        return savedAlbums.stream()
                .map(saved -> convertToAlbumDto(saved.getAlbum()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // ======================
    // PLAYLIST FOLLOWING
    // ======================

    public void followPlaylist(Long userId, Long playlistId) {
        if (!playlistRepository.existsById(playlistId)) {
            throw new PlaylistNotFoundException("Playlist not found with ID: " + playlistId);
        }

        UserSavedPlaylistId id = new UserSavedPlaylistId(userId, playlistId);

        if (!userSavedPlaylistRepository.existsById(id)) {
            UserSavedPlaylist followed = new UserSavedPlaylist();
            followed.setId(id);
            followed.setAddedAt(LocalDateTime.now());
            userSavedPlaylistRepository.save(followed);
        }else {
            throw new DuplicateEntryException("Playlist already followed by user");
        }
    }

    public void unfollowPlaylist(Long userId, Long playlistId) {
        UserSavedPlaylistId id = new UserSavedPlaylistId(userId, playlistId);
        userSavedPlaylistRepository.deleteById(id);
    }

    public List<PlaylistDto> getUserFollowedPlaylists(Long userId) {
        List<UserSavedPlaylist> savedPlaylists = userSavedPlaylistRepository.findByIdUserIdOrderByAddedAtDesc(userId);
        return savedPlaylists.stream()
                .map(saved -> convertToPlaylistDto(saved.getPlaylist()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // ======================
    // GET FULL USER LIBRARY
    // ======================

    public UserLibraryDto getUserLibrary(Long userId) {
        UserLibraryDto library = new UserLibraryDto();

        library.setSavedSongs(getUserSavedSongs(userId));
        library.setSavedAlbums(getUserSavedAlbums(userId));
        library.setFollowedArtists(getUserFollowedArtists(userId));
        library.setFollowedPlaylists(getUserFollowedPlaylists(userId));

        return library;
    }

    // ======================
    // HELPER CONVERSION METHODS
    // ======================

    private SongDto convertToSongDto(Song song) {
        if (song == null) return null;
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
        return dto;
    }

    private AlbumDto convertToAlbumDto(Album album) {
        if (album == null) return null;
        AlbumDto dto = new AlbumDto();
        dto.setAlbumId(album.getAlbumId());
        dto.setAlbumName(album.getAlbumName());
        dto.setArtistId(album.getArtistId());
        dto.setReleaseDate(album.getReleaseDate());
        dto.setAlbumType(album.getAlbumType());
        dto.setTotalTracks(album.getTotalTracks());
        dto.setCoverImageUrl(album.getCoverImageUrl());
        dto.setSpotifyUrl(album.getSpotifyUrl());
        return dto;
    }

    private PlaylistDto convertToPlaylistDto(Playlist playlist) {
        if (playlist == null) return null;
        PlaylistDto dto = new PlaylistDto();
        dto.setPlaylistId(playlist.getPlaylistId());
        dto.setPlaylistName(playlist.getPlaylistName());
        dto.setDescription(playlist.getDescription());
        dto.setIsPublic(playlist.getIsPublic());
        dto.setCreatedAt(playlist.getCreatedAt());
        dto.setUpdatedAt(playlist.getUpdatedAt());
        // songCount and songs populated separately if needed
        return dto;
    }
}