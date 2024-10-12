package com.crashcourse.kickoff.tms.club.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crashcourse.kickoff.tms.club.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    // add query methods (remember that each method's implementation is through the method name)
    Optional<Club> findByName(String name);  

    @Query(value = "SELECT * FROM club WHERE :playerId = ANY(players)", nativeQuery = true)
    Optional<Club> findClubByPlayerId(@Param("playerId") Long playerId);
}