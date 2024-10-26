package com.crashcourse.kickoff.tms.club.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crashcourse.kickoff.tms.club.dto.PlayerApplicationDTO;
import com.crashcourse.kickoff.tms.club.exception.*;
import com.crashcourse.kickoff.tms.club.model.*;
import com.crashcourse.kickoff.tms.club.repository.*;
import com.crashcourse.kickoff.tms.player.PlayerPosition;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@Service
public class ClubServiceImpl implements ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private PlayerApplicationRepository applicationRepository;

    @Autowired
    private ClubInvitationRepository clubInvitationRepository;

    /**
     * CLUB CRUD METHODS
     */

    // jparepository has automatically implemented crud methods
    @Transactional
    public Club createClub(@Valid Club club, Long creatorId) throws Exception {
        // // Find the PlayerProfile by ID
        // PlayerProfile creator = playerProfileRepository.findById(creatorId)
        // .orElseThrow(() -> new RuntimeException("PlayerProfile not found"));

        // club name not unique
        if (clubRepository.findByName(club.getName()).isPresent()) {
            throw new ClubAlreadyExistsException("Club name must be unique");
        }

        // set the player who created the club as the captain
        club.setCaptainId(creatorId);
        clubRepository.save(club);

        // player count exceeds the limit
        if (club.getPlayers().size() > Club.MAX_PLAYERS_IN_CLUB) {
            throw new PlayerLimitExceededException(String.format("A club cannot have more than %d players", Club.MAX_PLAYERS_IN_CLUB));
        }
        
        club = addPlayerToClub(club.getId(), creatorId);
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

    public List<Club> getClubsByIds(@Positive(message = "Club ID must be positive") List<Long> clubIds) {
        List<Club> clubs = clubRepository.findAllById(clubIds);
        if (clubs.size() != clubIds.size()) {
            throw new ClubNotFoundException("One or more clubs not found for the provided IDs.");
        }
        return clubs;
    }

    public Optional<Club> getClubByPlayerId(Long playerId) {
        return clubRepository.findClubByPlayerId(playerId);
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

    public void deleteClub(Long id) {
        if (!clubRepository.existsById(id)) {
            throw new ClubNotFoundException("Club with ID " + id + " not found");
        }
        clubRepository.deleteById(id);
    }

    /**
     * PLAYER-RELATED METHODS
     */

    public Club addPlayerToClub(Long clubId, Long playerId) throws Exception {
        // PlayerProfile player = playerProfileRepository.findById(playerId)
        // .orElseThrow(() -> new RuntimeException("PlayerProfile not found"));

        Club club = clubRepository.findById(clubId).orElseThrow(() -> 
            new ClubNotFoundException("Club with ID " + clubId + " not found"));

        if (club.getPlayers().size() >= Club.MAX_PLAYERS_IN_CLUB) {
            throw new PlayerLimitExceededException(String.format("A club cannot have more than %d players", Club.MAX_PLAYERS_IN_CLUB));
        }

        if (club.getPlayers().contains(playerId)) {
            throw new Exception("Player is already a member of this club");
        }

        club.getPlayers().add(playerId);  

        clubRepository.save(club);
        return club;
    }

    @Override
    public List<Long> getPlayers(Long clubId) {
        Optional<Club> clubOptional = clubRepository.findById(clubId);
        if (!clubOptional.isPresent()) {
            throw new ClubNotFoundException("Club with ID " + clubId + " not found");
        }

        Club club = clubOptional.get();
        return club.getPlayers();
    }

    public Club removePlayerFromClub(Long clubId, Long playerId) throws Exception {
        
        Club club = clubRepository.findById(clubId).orElseThrow(() -> 
            new ClubNotFoundException("Club with ID " + clubId + " not found"));

        boolean removed = club.getPlayers().remove(playerId);
        if (!removed) {
            throw new Exception("Player is not a member of this club");
        }

        return clubRepository.save(club);
    }

    // to transfer captain status to another player in the club
    public Club transferCaptaincy(Long clubId, Long currentCaptainId, Long newCaptainId) throws Exception {
        // PlayerProfile currentCaptain = playerProfileRepository.findById(currentCaptainId)
        // .orElseThrow(() -> new RuntimeException("currentCaptain not found"));

        // PlayerProfile newCaptain = playerProfileRepository.findById(newCaptainId)
        // .orElseThrow(() -> new RuntimeException("newCaptain not found"));
        
        Club club = clubRepository.findById(clubId).orElseThrow(() -> 
            new ClubNotFoundException("Club with ID " + clubId + " not found"));
    
        if (!club.getCaptainId().equals(currentCaptainId)) {
            throw new Exception("Only the current captain can transfer the captaincy.");
        }
    
        if (!club.getPlayers().contains(newCaptainId)) {
            throw new Exception("The new captain must be a player in the club.");
        }
    
        club.setCaptainId(newCaptainId);
        return clubRepository.save(club);
    }

    @Override
    public boolean isCaptain(Long clubId, Long playerId) {
        Club club = clubRepository.findById(clubId).orElse(null);
        return club != null && club.getCaptainId().equals(playerId);
    }

    /**
     * INVITATION METHODS (CLUB INVITES PLAYER)
     */

    @Override
    public Club invitePlayerToClub(Long clubId, Long playerId, Long captainId) throws Exception {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ClubNotFoundException("Club not found with ID: " + clubId));

        if (!club.getCaptainId().equals(captainId)) {
            throw new Exception("Only the club captain can invite players.");
        }

        ClubInvitation invitation = new ClubInvitation();
        invitation.setClub(club);
        invitation.setPlayerId(playerId);
        invitation.setStatus(ApplicationStatus.PENDING);
        invitation.setInviteSentDate(LocalDateTime.now());

        clubInvitationRepository.save(invitation);

        return club;
    }

    public Club acceptInvite(Long playerId, Long clubId) throws Exception {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new Exception("Club not found with id: " + clubId));

        if (club.getPlayers().size() >= Club.MAX_PLAYERS_IN_CLUB) {
            throw new PlayerLimitExceededException(String.format("A club cannot have more than %d players", Club.MAX_PLAYERS_IN_CLUB));
        }

        club.getPlayers().add(playerId);

        clubRepository.save(club);

        return club;
    }

    @Override
    public List<ClubInvitation> getPlayerInvitations(Long playerId) throws Exception {
        return clubInvitationRepository.findByPlayerIdAndStatus(playerId, ApplicationStatus.PENDING);
    }

    /**
     * APPLICATION METHODS (PLAYER APPLIES TO CLUB)
     */

    public void applyToClub(PlayerApplicationDTO applicationDTO) throws Exception {

        Long playerId = applicationDTO.getPlayerId(); 
        
        System.out.println("Club ID: " + applicationDTO.getClubId());
        Club club = clubRepository.findById(applicationDTO.getClubId())
                .orElseThrow(() -> new ClubNotFoundException("Club with ID " + applicationDTO.getClubId() + " not found"));
    
        // Check if the club is full
        if (club.getPlayers().size() >= Club.MAX_PLAYERS_IN_CLUB) {
            throw new PlayerLimitExceededException(
                String.format("A club cannot have more than %d players", Club.MAX_PLAYERS_IN_CLUB)
            );
        }
    
        // Check if the user has already applied to this club
        if (applicationRepository.existsByPlayerIdAndClub(playerId, club)) {  // Use 'User' instead of 'PlayerProfile'
            throw new PlayerAlreadyAppliedException("Player has already applied to this club");
        }
    
        // Create a new PlayerApplication
        PlayerApplication application = new PlayerApplication();
        application.setClub(club);
        application.setPlayerId(playerId);  
        application.setDesiredPosition(applicationDTO.getDesiredPosition());
        application.setStatus(ApplicationStatus.PENDING);
    
        // Save the application
        applicationRepository.save(application);

        /*
        * Add Applicant to Club
        */
        club.getApplicants().add(application.getId());
        clubRepository.save(club);
    }

    @Override
    public List<Long> getPlayerApplications(Long clubId) {
        Optional<Club> clubOptional = clubRepository.findById(clubId);
        if (!clubOptional.isPresent()) {
            throw new ClubNotFoundException("Club with ID " + clubId + " not found");
        }
        Club club = clubOptional.get();

        List<Long> playerIds = new ArrayList<>();

        for (Long applicationId: club.getApplicants()) {
            Optional<PlayerApplication> applicationOptional = applicationRepository.findById(applicationId);
            playerIds.add(applicationOptional.get().getPlayerId());
        }
        return playerIds;
    }

    @Transactional
    @Override
    public void acceptApplication(Long clubId, Long playerId) {
        Optional<Club> clubOptional = clubRepository.findById(clubId);
        if (!clubOptional.isPresent()) {
            throw new ClubNotFoundException("Club with ID " + clubId + " not found");
        }
        Club club = clubOptional.get();

        /*
         * Find the application
         */
        PlayerApplication playerApplication = applicationRepository.findByClubIdAndPlayerId(clubId, playerId);

        /*
         * Add to Players list
         * Remove from applicants list
         */
        club.getPlayers().add(playerId);
        club.getApplicants().remove(playerApplication.getId());
        clubRepository.save(club);


        /*
         * Remove application from repository
         */
        applicationRepository.deleteById(playerApplication.getId());        
    }

    @Transactional
    @Override
    public void rejectApplication(Long clubId, Long playerId) {
        
        Optional<Club> clubOptional = clubRepository.findById(clubId);
        if (!clubOptional.isPresent()) {
            throw new ClubNotFoundException("Club with ID " + clubId + " not found");
        }
        Club club = clubOptional.get();

        /*
         * Find the application
         */
        PlayerApplication playerApplication = applicationRepository.findByClubIdAndPlayerId(clubId, playerId);

        /*
         * Remove from applicants list
         */
        club.getApplicants().remove(playerApplication.getId());
        clubRepository.save(club);

        /*
         * Remove application from repository
         */
        System.out.println("THREE");
        applicationRepository.deleteById(playerApplication.getId());
    }

    @Transactional
    @Override
    public Club playerLeaveClub(Long clubId, Long playerId) throws Exception {
        Club club = clubRepository.findById(clubId).orElseThrow(() -> 
            new ClubNotFoundException("Club with ID " + clubId + " not found"));
        System.out.println("Club and Player IDs: " + clubId + ", " + playerId);
    
        if (club.getCaptainId().equals(playerId)) {
            // If the captain is the only player, disband the club
            if (club.getPlayers().size() == 1) {
                System.out.println("Disbanding club as player is the last member and captain.");
                clubRepository.deleteById(clubId);
                return null;
            } else {
                throw new Exception("You must transfer the captaincy before leaving the club.");
            }
        }
    
        // Remove the player from the club if they are not the captain
        boolean removed = club.getPlayers().remove(playerId);
        System.out.println("Player removed from club: " + removed);
        if (!removed) {
            throw new Exception("Player is not a member of this club.");
        }
    
        return clubRepository.save(club);
    }


}