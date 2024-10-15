package com.crashcourse.kickoff.tms.tournament.repository;

import com.crashcourse.kickoff.tms.tournament.model.PlayerAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerAvailabilityRepository extends JpaRepository<PlayerAvailability, Long> {
    List<PlayerAvailability> findByTournamentId(Long tournamentId);
    Optional<PlayerAvailability> findByTournamentIdAndPlayerId(Long tournamentId, Long playerId);
}
