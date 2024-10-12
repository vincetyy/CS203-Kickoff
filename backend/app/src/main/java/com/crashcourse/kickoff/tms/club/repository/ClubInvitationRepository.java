package com.crashcourse.kickoff.tms.club.repository;

import com.crashcourse.kickoff.tms.club.model.ClubInvitation;
import com.crashcourse.kickoff.tms.club.model.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubInvitationRepository extends JpaRepository<ClubInvitation, Long> {
    List<ClubInvitation> findByPlayerIdAndStatus(Long playerId, ApplicationStatus status);
}
