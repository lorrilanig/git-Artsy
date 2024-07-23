package org.launchcode.git_artsy_backend.controllers;

import org.launchcode.git_artsy_backend.models.Profile;
import org.launchcode.git_artsy_backend.repositories.ProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/artist-profiles")
@CrossOrigin(origins = "http://localhost:8082")
public class ProfileController {
    @Autowired
    private ProfileRepo profileRepo;

    public ResponseEntity<Profile> createArtistProfile(@RequestBody Profile artistProfile) {
        artistProfile.setCreatedAt(LocalDateTime.now());
        artistProfile.setUpdatedAt(LocalDateTime.now());
        try {
            Profile savedArtistProfile = profileRepo.save(artistProfile);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArtistProfile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<Profile>> getAllArtistProfiles() {
        List<Profile> artistProfiles = profileRepo.findAll();
        return ResponseEntity.ok(artistProfiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getArtistProfileById(@PathVariable Integer id) {
        Optional<Profile> optionalArtistProfile = profileRepo.findById(id);
        if (optionalArtistProfile.isPresent()) {
            return ResponseEntity.ok(optionalArtistProfile.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateArtistProfile(@PathVariable Integer id, @RequestBody Profile updatedArtistProfile) {
        Optional<Profile> existingArtistProfile = profileRepo.findById(id);
        if (existingArtistProfile.isPresent()) {
            Profile artistProfileToUpdate = existingArtistProfile.get();
            artistProfileToUpdate.setName(updatedArtistProfile.getName());
            artistProfileToUpdate.setLocation(updatedArtistProfile.getLocation());
            artistProfileToUpdate.setEmail(updatedArtistProfile.getEmail());
            artistProfileToUpdate.setPhone(updatedArtistProfile.getPhone());
            artistProfileToUpdate.setProfilePic(updatedArtistProfile.getProfilePic());
            artistProfileToUpdate.setBioDescription(updatedArtistProfile.getBioDescription());
            artistProfileToUpdate.setUpdatedAt(LocalDateTime.now());
            try {
                Profile savedArtistProfile = profileRepo.save(artistProfileToUpdate);
                return ResponseEntity.ok(savedArtistProfile);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtistProfile(@PathVariable Integer id) {
        Optional<Profile> existingArtistProfile = profileRepo.findById(id);
        if (existingArtistProfile.isPresent()) {
            try {
                profileRepo.deleteById(id);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
