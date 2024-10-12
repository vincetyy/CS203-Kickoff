package com.crashcourse.kickoff.tms.tournament.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlayerAvailabilityDTO {
    private Long tournamentId;
    private Long playerId;
    private boolean isAvailable;
}
