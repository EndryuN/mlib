// src/main/java/com/mlib/backend/controller/SearchController.java
package com.mlib.backend.controller;

import com.mlib.backend.model.Artist;
import com.mlib.backend.model.Song;
import com.mlib.backend.repository.ArtistRepository;
import com.mlib.backend.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    /**
     * Search artists by name or alias
     */
    @GetMapping("/artists")
    public ResponseEntity<List<Artist>> searchArtists(@RequestParam String q) {
        List<Artist> results = artistRepository.searchArtists(q);
        return ResponseEntity.ok(results);
    }

    /**
     * Search songs by name or featured artists
     */
    @GetMapping("/songs")
    public ResponseEntity<List<Song>> searchSongs(@RequestParam String q) {
        List<Song> results = songRepository.searchSongs(q);
        return ResponseEntity.ok(results);
    }
}