package com.crashcourse.kickoff.tms.tournament.dto;

import com.crashcourse.kickoff.tms.location.model.*;
import com.crashcourse.kickoff.tms.tournament.model.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for creating a Tournament.
 * This DTO captures all necessary information required to instantiate a Tournament.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentUpdateDTO {

    @NotBlank(message = "Tournament name is required")
    private String name;

    @NotNull(message = "Start date and time is required")
    private LocalDateTime startDateTime;
    @NotNull(message = "End date and time is required")
    private LocalDateTime endDateTime;

    @NotNull(message = "Location ID is required")
    private Location location;

    private List<@Positive(message = "Prize amount must be positive") Float> prizePool;

    @Min(value = 0, message = "Minimum rank cannot be negative")
    private Integer minRank;
    @Min(value = 0, message = "Maximum rank cannot be negative")
    private Integer maxRank;

}
