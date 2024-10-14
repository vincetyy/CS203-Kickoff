package com.crashcourse.kickoff.tms.tournament.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crashcourse.kickoff.tms.tournament.model.Tournament;  

/*
 * Responsible for interactions with database
 * storing all created tournament objects
 */

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    // Find upcoming tournaments for a specific club
    @Query("SELECT t FROM Tournament t WHERE :clubId MEMBER OF t.joinedClubIds AND t.startDateTime > CURRENT_TIMESTAMP")
    List<Tournament> findUpcomingTournamentsForClub(@Param("clubId") Long clubId);

    // Find current tournaments for a specific club
    @Query("SELECT t FROM Tournament t WHERE :clubId MEMBER OF t.joinedClubIds AND t.startDateTime <= CURRENT_TIMESTAMP AND t.endDateTime >= CURRENT_TIMESTAMP")
    List<Tournament> findCurrentTournamentsForClub(@Param("clubId") Long clubId);

    // Find past tournaments for a specific club
    @Query("SELECT t FROM Tournament t WHERE :clubId MEMBER OF t.joinedClubIds AND t.endDateTime < CURRENT_TIMESTAMP")
    List<Tournament> findPastTournamentsForClub(@Param("clubId") Long clubId);
}
