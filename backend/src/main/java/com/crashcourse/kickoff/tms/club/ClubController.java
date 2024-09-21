package com.crashcourse.kickoff.tms.club;

import com.crashcourse.kickoff.tms.club.exception.*;
import com.crashcourse.kickoff.tms.user.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/club")
public class ClubController {

    @Autowired
    private ClubService clubService;

    @PostMapping("/create")
    public ResponseEntity<?> createClub(@Valid @RequestBody Club club, @RequestBody User creator) {
        try {
            Club createdClub = clubService.createClub(club, creator);
            return new ResponseEntity<>(createdClub, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public List<Club> getAllClubs() {
        return clubService.getAllClubs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClubById(@PathVariable Long id) {
        Optional<Club> club = clubService.getClubById(id);
        return club.map(ResponseEntity::ok)
                   .orElseGet(() -> new ResponseEntity<>("Club not found", HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateClub(@PathVariable Long id, @RequestBody Club clubDetails) {
        try {
            Club updatedClub = clubService.updateClub(id, clubDetails);
            return new ResponseEntity<>(updatedClub, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);
        return new ResponseEntity<>("Club deleted successfully", HttpStatus.OK);
    }
}