package com.crashcourse.kickoff.tms.match.repository;

import com.crashcourse.kickoff.tms.match.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Match entities.
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    // Add custom query methods if needed
}
