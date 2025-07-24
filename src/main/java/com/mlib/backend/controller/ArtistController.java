package com.mlib.backend.controller;

import com.mlib.backend.model.Artist;
import com.mlib.backend.model.Song;
import com.mlib.backend.repository.ArtistRepository;
import com.mlib.backend.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable Integer id) {
        return artistRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getSongsByArtist(@PathVariable Integer id) {
        List<Song> songs = songRepository.findByArtistId(id);
        return ResponseEntity.ok(songs);
    }
}