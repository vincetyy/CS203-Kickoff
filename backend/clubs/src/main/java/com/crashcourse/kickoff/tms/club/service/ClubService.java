package com.crashcourse.kickoff.tms.club.service;

import java.util.List;
import java.util.Optional;

import com.crashcourse.kickoff.tms.club.dto.PlayerApplicationDTO;
import com.crashcourse.kickoff.tms.club.model.Club;
import com.crashcourse.kickoff.tms.club.model.ClubInvitation;

import jakarta.validation.Valid;

public interface ClubService {

    Club createClub(@Valid Club club, Long creatorId) throws Exception;

    List<Club> getAllClubs();

    Optional<Club> getClubById(Long id);

    void deleteClub(Long id);

    Club transferCaptaincy(Long clubId, Long currentCaptainId, Long newCaptainId) throws Exception;

    Club updateClub(Long id, Club clubDetails);

    Club addPlayerToClub(Long clubId, Long playerId) throws Exception;

    Club removePlayerFromClub(Long clubId, Long playerId) throws Exception;

    Club invitePlayerToClub(Long clubId, Long playerId, Long captainId) throws Exception;

    Club acceptInvite(Long playerId, Long clubId) throws Exception;

    void applyToClub(PlayerApplicationDTO applicationDTO) throws Exception;

    List<Club> getClubsByIds(List<Long> clubIds);

    List<ClubInvitation> getPlayerInvitations(Long playerId) throws Exception;

    boolean isCaptain(Long clubId, Long playerId);

    List<Long> getPlayers(Long clubId);

    Optional<Club> getClubByPlayerId(Long playerId);
}