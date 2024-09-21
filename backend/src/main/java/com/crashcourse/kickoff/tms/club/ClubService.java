package com.example.tournamentmanagement.service;

import com.example.tournamentmanagement.entity.Club;
import com.example.tournamentmanagement.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    public Club createClub(Club club) {
        return clubRepository.save(club);
    }

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Optional<Club> getClubById(Long id) {
        return clubRepository.findById(id);
    }
    
    public void deleteClub(Long id) {
        clubRepository.deleteById(id);
    }

    // Update an existing club (can use this when you need to add or remove players)
    public Club updateClub(Long id, Club clubDetails) {
        Optional<Club> clubOptional = clubRepository.findById(id);
        if (clubOptional.isPresent()) {
            // if you have more club instance variables, call setter for all of them
            Club club = clubOptional.get();
            club.setName(clubDetails.getName());
            club.setElo(clubDetails.getElo());
            club.setRatingDeviation(clubDetails.getRatingDeviation());

            return clubRepository.save(club);
        }

        // if club doesn't exist, return null
        return null;
    }
}