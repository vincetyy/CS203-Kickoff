package com.crashcourse.kickoff.tms.match.repository;

import com.crashcourse.kickoff.tms.match.model.SingleEliminationBracket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Match entities.
 */
@Repository
public interface SingleEliminationBracketRepository extends JpaRepository<SingleEliminationBracket, Long> {
    // Add custom query methods if needed
}
