package com.crashcourse.kickoff.tms.match.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for responding with Tournament data.
 * This DTO excludes sensitive or unnecessary fields and presents data in a client-friendly format.
 */
@Data
@AllArgsConstructor
public class MatchResponseDTO {
    private Long matchId;
    private boolean isOver;
    
    private Long tournamentId;

    /*
     * Clubs
     */
    private Long club1Id;
    private Long club2Id;

    /*
     * Score
     */
    private int club1Score;
    private int club2Score;

    private Long winningClubId;
}
