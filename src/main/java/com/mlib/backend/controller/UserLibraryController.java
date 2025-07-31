package com.mlib.backend.controller;

import com.mlib.backend.dto.AlbumDto;
import com.mlib.backend.dto.ArtistDto;
import com.mlib.backend.dto.SongDto;
import com.mlib.backend.dto.UserLibraryDto;
import com.mlib.backend.exception.SongNotFoundException;
import com.mlib.backend.exception.AlbumNotFoundException;
import com.mlib.backend.exception.ArtistNotFoundException;
import com.mlib.backend.service.UserLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@CrossOrigin(origins = "*")
public class UserLibraryController {

    @Autowired
    private UserLibraryService userLibraryService;

    /**
     * Get the full user library (songs, albums, artists).
     *
     * @param authentication The authenticated user.
     * @return ResponseEntity containing the user's library.
     */
    @GetMapping
    public ResponseEntity<UserLibraryDto> getUserLibrary(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        UserLibraryDto library = userLibraryService.getUserLibrary(userId);
        return ResponseEntity.ok(library);
    }

    // -------------------------
    // Song Favorites Endpoints
    // -------------------------

    /**
     * Save a song to the user's favorites.
     *
     * @param songId         The ID of the song to save.
     * @param authentication The authenticated user.
     * @return ResponseEntity indicating success.
     */
    @PostMapping("/songs/{songId}")
    public ResponseEntity<Void> saveSong(
            @PathVariable Integer songId,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        try {
            userLibraryService.saveSong(userId, songId);
        } catch (SongNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Remove a song from the user's favorites.
     *
     * @param songId         The ID of the song to remove.
     * @param authentication The authenticated user.
     * @return ResponseEntity indicating success.
     */
    @DeleteMapping("/songs/{songId}")
    public ResponseEntity<Void> unsaveSong(
            @PathVariable Integer songId,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        userLibraryService.unsaveSong(userId, songId);
        return ResponseEntity.ok().build();
    }

    /**
     * Get all saved songs for the user.
     *
     * @param authentication The authenticated user.
     * @return ResponseEntity containing the list of saved songs.
     */
    @GetMapping("/songs")
    public ResponseEntity<List<SongDto>> getSavedSongs(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<SongDto> songs = userLibraryService.getUserSavedSongs(userId);
        return ResponseEntity.ok(songs);
    }

    /**
     * Check if a song is saved by the user.
     *
     * @param songId         The ID of the song to check.
     * @param authentication The authenticated user.
     * @return ResponseEntity containing true/false.
     */
    @GetMapping("/songs/{songId}/saved")
    public ResponseEntity<Boolean> isSongSaved(
            @PathVariable Integer songId,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        boolean saved = userLibraryService.isSongSaved(userId, songId);
        return ResponseEntity.ok(saved);
    }

    // -------------------------
    // Album Favorites Endpoints
    // -------------------------

    /**
     * Save an album to the user's favorites.
     *
     * @param albumId        The ID of the album to save.
     * @param authentication The authenticated user.
     * @return ResponseEntity indicating success.
     */
    @PostMapping("/albums/{albumId}")
    public ResponseEntity<Void> saveAlbum(
            @PathVariable Integer albumId,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        try {
            userLibraryService.saveAlbum(userId, albumId);
        } catch (AlbumNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Remove an album from the user's favorites.
     *
     * @param albumId        The ID of the album to remove.
     * @param authentication The authenticated user.
     * @return ResponseEntity indicating success.
     */
    @DeleteMapping("/albums/{albumId}")
    public ResponseEntity<Void> unsaveAlbum(
            @PathVariable Integer albumId,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        userLibraryService.unsaveAlbum(userId, albumId);
        return ResponseEntity.ok().build();
    }

    /**
     * Get all saved albums for the user.
     *
     * @param authentication The authenticated user.
     * @return ResponseEntity containing the list of saved albums.
     */
    @GetMapping("/albums")
    public ResponseEntity<List<AlbumDto>> getSavedAlbums(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<AlbumDto> albums = userLibraryService.getUserSavedAlbums(userId);
        return ResponseEntity.ok(albums);
    }

    // -------------------------
    // Artist Following Endpoints
    // -------------------------

    /**
     * Follow an artist.
     *
     * @param artistUri       The ID of the artist to follow.
     * @param authentication The authenticated user.
     * @return ResponseEntity indicating success.
     */
    @PostMapping("/artists/{artistId}")
    public ResponseEntity<Void> followArtist(
            @PathVariable String artistUri,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        try {
            userLibraryService.followArtist(userId, artistUri);
        } catch (ArtistNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Unfollow an artist.
     *
     * @param artistUri       The ID of the artist to unfollow.
     * @param authentication The authenticated user.
     * @return ResponseEntity indicating success.
     */
    @DeleteMapping("/artists/{artistId}")
    public ResponseEntity<Void> unfollowArtist(
            @PathVariable String artistUri,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        userLibraryService.unfollowArtist(userId, artistUri);
        return ResponseEntity.ok().build();
    }

    /**
     * Get all followed artists for the user.
     *
     * @param authentication The authenticated user.
     * @return ResponseEntity containing the list of followed artists.
     */
    @GetMapping("/artists")
    public ResponseEntity<List<ArtistDto>> getFollowedArtists(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<ArtistDto> artists = userLibraryService.getUserFollowedArtists(userId);
        return ResponseEntity.ok(artists);
    }

    /**
     * Extract the user ID from the authentication token.
     *
     * @param authentication The authenticated user.
     * @return The user's ID.
     */
    private Long getUserIdFromAuthentication(Authentication authentication) {
        // Example: Extract user ID from JWT token or authentication principal
        // Replace with actual implementation based on your authentication setup
        return 1L; // Placeholder
    }
}