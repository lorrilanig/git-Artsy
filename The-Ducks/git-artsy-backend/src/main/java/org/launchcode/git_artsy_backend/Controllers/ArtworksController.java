package org.launchcode.git_artsy_backend.Controllers;

import org.launchcode.git_artsy_backend.Models.Artworks;
import org.launchcode.git_artsy_backend.Models.dto.ArtworksDto;
import org.launchcode.git_artsy_backend.Repo.ArtworksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/artworks")
@CrossOrigin(origins = "http://localhost:8082")
public class ArtworksController {

    @Autowired
    private ArtworksRepo artworkRepo;

    @PostMapping("/new")
    public Artworks createArtwork(@RequestBody Artworks artwork) {
        artwork.setCreatedAt(LocalDateTime.now());
        artwork.setUpdatedAt(LocalDateTime.now());
        try {
            return artworkRepo.save(artwork);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create artwork");
        }
    }

    @GetMapping("/all")
    public List<Artworks> getAllArtworks() {
        return artworkRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artworks> getArtworkById(@PathVariable Integer id) {
        Optional<Artworks> optionalArtwork = artworkRepo.findById(id);

        if (optionalArtwork.isPresent()) {
            return ResponseEntity.ok(optionalArtwork.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artworks> updateArtwork(@PathVariable Integer id, @RequestBody Artworks updatedArtwork) {
        Optional<Artworks> existingArtwork = artworkRepo.findById(id);

        if (existingArtwork.isPresent()) {

            Artworks artworkToUpdate = existingArtwork.get();
            artworkToUpdate.setTitle(updatedArtwork.getTitle());
            artworkToUpdate.setDescription(updatedArtwork.getDescription());
            artworkToUpdate.setPrice(updatedArtwork.getPrice());
            artworkToUpdate.setImageUrl(updatedArtwork.getImageUrl());
            artworkToUpdate.setUpdatedAt(LocalDateTime.now());
            try {
                Artworks savedArtwork = artworkRepo.save(artworkToUpdate);
                return ResponseEntity.ok(savedArtwork);
            } catch (Exception e) {
                // Handle database or save errors
                e.printStackTrace(); // Log the exception
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtwork(@PathVariable Integer id) {
        Optional<Artworks> existingArtwork = artworkRepo.findById(id);

        if (existingArtwork.isPresent()) {
            try {
                artworkRepo.deleteById(id);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                // Handle database or delete errors
                e.printStackTrace(); // Log the exception
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            // Artwork with given ID not found
            return ResponseEntity.notFound().build();
        }
    }
}
