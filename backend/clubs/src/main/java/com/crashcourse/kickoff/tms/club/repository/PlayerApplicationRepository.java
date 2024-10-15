package com.crashcourse.kickoff.tms.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crashcourse.kickoff.tms.club.model.Club;
import com.crashcourse.kickoff.tms.club.model.PlayerApplication;

public interface PlayerApplicationRepository extends JpaRepository<PlayerApplication, Long> {
    boolean existsByPlayerIdAndClub(Long playerId, Club club);  // Should accept User and Club
    PlayerApplication findByClubIdAndPlayerId(Long clubId, Long playerId);
}
