package com.crashcourse.kickoff.tms.player.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crashcourse.kickoff.tms.player.PlayerProfile;

@Repository
public interface PlayerProfileRepository extends JpaRepository<PlayerProfile, Long> {
    // Spring Data JPA will automatically implement common methods such as findById, save, delete, etc.
}