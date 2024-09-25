package com.crashcourse.kickoff.tms.location.repository;

import com.crashcourse.kickoff.tms.location.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Location entities.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    // Add custom query methods if needed
}
