package com.crashcourse.kickoff.tms.tournament.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerAvailabilityDTO {
    private Long tournamentId;  
    private Long playerId;
    private Long clubId;
    private boolean isAvailable;
}
