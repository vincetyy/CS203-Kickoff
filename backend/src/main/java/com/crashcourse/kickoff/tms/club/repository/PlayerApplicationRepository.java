package com.crashcourse.kickoff.tms.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.crashcourse.kickoff.tms.club.model.PlayerApplication;
import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.club.Club;

public interface PlayerApplicationRepository extends JpaRepository<PlayerApplication, Long> {
    boolean existsByPlayerProfileAndClub(PlayerProfile playerProfile, Club club);  // Should accept User and Club
}
