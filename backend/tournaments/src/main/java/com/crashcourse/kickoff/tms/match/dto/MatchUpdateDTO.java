package com.crashcourse.kickoff.tms.match.dto;


import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class MatchUpdateDTO {
    private Long matchId;
    private boolean isOver;
    
    /*
     * Clubs
     */
    private Long club1Id;
    private Long club2Id;

    /*
     * Score
     */
    int club1Score;
    int club2Score;

    Long winningClubId;
}
