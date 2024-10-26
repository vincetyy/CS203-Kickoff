
package com.crashcourse.kickoff.tms.match.repository;

import com.crashcourse.kickoff.tms.match.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {
    Round findRoundByRoundNumber(Long roundNumber);
}
