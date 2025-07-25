// src/main/java/com/mlib/backend/controller/SearchController.java
package com.mlib.backend.controller;

import com.mlib.backend.dto.ArtistDto;
import com.mlib.backend.dto.SongDto;
import com.mlib.backend.model.Artist;
import com.mlib.backend.model.Song;
import com.mlib.backend.repository.ArtistRepository;
import com.mlib.backend.repository.SongRepository;
import com.mlib.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SearchService searchService;

    /**
     * Search artists by name or alias
     */
    @GetMapping("/artists")
    public ResponseEntity<List<ArtistDto>> searchArtists(@RequestParam String q) {
        return ResponseEntity.ok(searchService.searchArtists(q));
    }

    @GetMapping("/songs")
    public ResponseEntity<List<SongDto>> searchSongs(@RequestParam String q) {
        return ResponseEntity.ok(searchService.searchSongs(q));
    }

    @GetMapping("/songs/language/{lang}")
    public ResponseEntity<List<SongDto>> filterByLanguage(@PathVariable String lang) {
        return ResponseEntity.ok(searchService.filterByLanguage(lang));
    }

    @GetMapping("/songs/status")
    public ResponseEntity<List<SongDto>> filterByStatus(@RequestParam List<String> statuses) {
        return ResponseEntity.ok(searchService.filterByReleaseStatus(statuses));
    }
}