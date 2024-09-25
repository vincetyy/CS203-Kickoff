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

public interface ClubService {

    Club createClub(@Valid Club club, Long creatorId);

    List<Club> getAllClubs();

    Optional<Club> getClubById(Long id);

    void deleteClub(Long id);

    Club transferCaptaincy(Long clubId, Long currentCaptainId, Long newCaptainId) throws Exception;

    Club updateClub(Long id, Club clubDetails);

    Club addPlayerToClub(Long clubId, Long playerId) throws Exception;

    Club removePlayerFromClub(Long clubId, Long playerId) throws Exception;

    void applyToClub(PlayerApplicationDTO applicationDTO) throws Exception;
}