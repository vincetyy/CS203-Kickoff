package com.crashcourse.kickoff.tms.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.crashcourse.kickoff.tms.club.model.PlayerApplication;
import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.club.Club;

public interface PlayerApplicationRepository extends JpaRepository<PlayerApplication, Long> {
    boolean existsByPlayerAndClub(User player, Club club);  // Should accept User and Club
}
