package com.crashcourse.kickoff.tms.user.repository;

import com.crashcourse.kickoff.tms.user.model.PlayerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerProfileRepository extends JpaRepository<PlayerProfile, Long> {
    // Spring Data JPA will automatically implement common methods such as findById, save, delete, etc.
}