package com.crashcourse.kickoff.tms.user.repository;

import com.crashcourse.kickoff.tms.user.model.HostProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostProfileRepository extends JpaRepository<HostProfile, Long> {
    // Spring Data JPA will automatically implement common methods such as findById, save, delete, etc.
}