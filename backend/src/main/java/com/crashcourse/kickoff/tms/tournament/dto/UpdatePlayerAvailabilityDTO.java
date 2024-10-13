package com.crashcourse.kickoff.tms.tournament.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlayerAvailabilityDTO {
    private Long tournamentId;
    private Long playerId;
    
    @JsonProperty("isAvailable")
    private boolean available;

    @JsonProperty("isAvailable")
    public boolean isAvailable() {
        return available;
    }

    @JsonProperty("isAvailable")
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
