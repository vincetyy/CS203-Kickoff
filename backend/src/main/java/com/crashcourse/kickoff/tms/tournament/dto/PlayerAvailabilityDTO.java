package com.crashcourse.kickoff.tms.tournament.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerAvailabilityDTO {
    private Long playerId;
    private String playerName;
    private boolean isAvailable;
}
