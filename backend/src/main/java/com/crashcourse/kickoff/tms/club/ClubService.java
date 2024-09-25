package com.crashcourse.kickoff.tms.club;

import com.crashcourse.kickoff.tms.user.model.*;
import com.crashcourse.kickoff.tms.user.repository.PlayerProfileRepository;
import com.crashcourse.kickoff.tms.club.exception.ClubNotFoundException;
import com.crashcourse.kickoff.tms.club.exception.PlayerLimitExceededException;
import com.crashcourse.kickoff.tms.club.exception.ClubAlreadyExistsException;

import com.crashcourse.kickoff.tms.club.dto.PlayerApplicationDTO;
import com.crashcourse.kickoff.tms.club.model.PlayerApplication;
import com.crashcourse.kickoff.tms.club.model.ApplicationStatus;
import com.crashcourse.kickoff.tms.club.repository.ClubRepository;
import com.crashcourse.kickoff.tms.club.repository.PlayerApplicationRepository;
import com.crashcourse.kickoff.tms.club.exception.PlayerAlreadyAppliedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private PlayerProfileRepository playerProfileRepository;

    @Autowired
    private PlayerApplicationRepository applicationRepository;

    // jparepository has automatically implemented crud methods
    public Club createClub(@Valid Club club, Long creatorId) {

        // Find the PlayerProfile by ID
        PlayerProfile creator = playerProfileRepository.findById(creatorId)
        .orElseThrow(() -> new RuntimeException("PlayerProfile not found"));

        // club name not unique
        if (clubRepository.findByName(club.getName()).isPresent()) {
            throw new ClubAlreadyExistsException("Club name must be unique");
        }

        // set the player who created the club as the captain
        club.setCaptain(creator);

        // player count exceeds the limit
        if (club.getPlayers().size() > Club.MAX_PLAYERS_IN_CLUB) {
            throw new PlayerLimitExceededException(String.format("A club cannot have more than %d players", Club.MAX_PLAYERS_IN_CLUB));
        }

        return clubRepository.save(club);
    }

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Optional<Club> getClubById(Long id) {
        Optional<Club> club = clubRepository.findById(id);
        if (!club.isPresent()) {
            throw new ClubNotFoundException("Club with ID " + id + " not found");
        }
        return club;
    }

    public void deleteClub(Long id) {
        if (!clubRepository.existsById(id)) {
            throw new ClubNotFoundException("Club with ID " + id + " not found");
        }
        clubRepository.deleteById(id);
    }

    // to transfer captain status to another player in the club
    public Club transferCaptaincy(Long clubId, Long currentCaptainId, Long newCaptainId) throws Exception {
        PlayerProfile currentCaptain = playerProfileRepository.findById(currentCaptainId)
        .orElseThrow(() -> new RuntimeException("currentCaptain not found"));

        PlayerProfile newCaptain = playerProfileRepository.findById(newCaptainId)
        .orElseThrow(() -> new RuntimeException("newCaptain not found"));
        
        Club club = clubRepository.findById(clubId).orElseThrow(() -> 
            new ClubNotFoundException("Club with ID " + clubId + " not found"));
    
        if (!club.getCaptain().equals(currentCaptain)) {
            throw new Exception("Only the current captain can transfer the captaincy.");
        }
    
        if (!club.getPlayers().contains(newCaptain)) {
            throw new Exception("The new captain must be a player in the club.");
        }
    
        club.setCaptain(newCaptain);
        return clubRepository.save(club);
    }

    // general method to update club, if there's common use case for a specific method (eg. updateElo), we can make that too
    public Club updateClub(Long id, Club clubDetails) {
        Optional<Club> clubOptional = clubRepository.findById(id);
        if (clubOptional.isPresent()) {
            Club club = clubOptional.get();
            
            // new name is not unique
            if (!club.getName().equals(clubDetails.getName()) &&
                clubRepository.findByName(clubDetails.getName()).isPresent()) {
                throw new ClubAlreadyExistsException("Club name must be unique");
            }
            
            // call setters to change deets
            club.setName(clubDetails.getName());
            club.setElo(clubDetails.getElo());
            club.setRatingDeviation(clubDetails.getRatingDeviation());
            
            return clubRepository.save(club);
        }

        // no such club to update
        throw new ClubNotFoundException("Club with ID " + id + " not found");
    }

    // add a player to club
    public Club addPlayerToClub(Long clubId, Long playerId) throws Exception {
        PlayerProfile player = playerProfileRepository.findById(playerId)
        .orElseThrow(() -> new RuntimeException("PlayerProfile not found"));

        Club club = clubRepository.findById(clubId).orElseThrow(() -> 
            new ClubNotFoundException("Club with ID " + clubId + " not found"));

        if (club.getPlayers().size() >= Club.MAX_PLAYERS_IN_CLUB) {
            throw new PlayerLimitExceededException(String.format("A club cannot have more than %d players", Club.MAX_PLAYERS_IN_CLUB));
        }

        if (club.getPlayers().contains(player)) {
            throw new Exception("Player is already a member of this club");
        }

        club.getPlayers().add(player);  
        return clubRepository.save(club);
    }

    // remove a player from club
    public Club removePlayerFromClub(Long clubId, Long playerId) throws Exception {
        PlayerProfile player = playerProfileRepository.findById(playerId)
        .orElseThrow(() -> new RuntimeException("PlayerProfile not found"));
        
        Club club = clubRepository.findById(clubId).orElseThrow(() -> 
            new ClubNotFoundException("Club with ID " + clubId + " not found"));

        boolean removed = club.getPlayers().remove(player);
        if (!removed) {
            throw new Exception("Player is not a member of this club");
        }

        return clubRepository.save(club);
    }

    public void applyToClub(PlayerApplicationDTO applicationDTO) throws Exception {
        // Fetch the club by ID
        Club club = clubRepository.findById(applicationDTO.getClubId())
                .orElseThrow(() -> new ClubNotFoundException("Club with ID " + applicationDTO.getClubId() + " not found"));
    
        // Fetch the PlayerProfile by playerId
        PlayerProfile playerProfile = playerProfileRepository.findById(applicationDTO.getPlayerId())
                .orElseThrow(() -> new RuntimeException("PlayerProfile not found"));
    
        // Get the User from PlayerProfile
        User user = playerProfile.getUser();
    
        // Check if the club is full
        if (club.getPlayers().size() >= Club.MAX_PLAYERS_IN_CLUB) {
            throw new PlayerLimitExceededException(
                String.format("A club cannot have more than %d players", Club.MAX_PLAYERS_IN_CLUB)
            );
        }
    
        // Check if the user has already applied to this club
        if (applicationRepository.existsByPlayerAndClub(user, club)) {  // Use 'User' instead of 'PlayerProfile'
            throw new PlayerAlreadyAppliedException("Player has already applied to this club");
        }
    
        // Create a new PlayerApplication
        PlayerApplication application = new PlayerApplication();
        application.setClub(club);
        application.setPlayer(user);  // Set the User, not the PlayerProfile
        application.setDesiredPosition(applicationDTO.getDesiredPosition());
        application.setStatus(ApplicationStatus.PENDING);
    
        // Save the application
        applicationRepository.save(application);
    }
}