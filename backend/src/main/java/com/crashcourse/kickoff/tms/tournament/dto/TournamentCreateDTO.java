package com.crashcourse.kickoff.tms.tournament.dto;

import com.crashcourse.kickoff.tms.host.HostProfile;
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
public class TournamentCreateDTO {

    /**
     * Name of the tournament.
     */
    @NotBlank(message = "Tournament name is required")
    private String name;

    /**
     * Start date and time of the tournament.
     */
    @NotNull(message = "Start date and time is required")
    private LocalDateTime startDateTime;

    /**
     * End date and time of the tournament.
     */
    @NotNull(message = "End date and time is required")
    private LocalDateTime endDateTime;

    /**
     * Identifier for the location where the tournament will be held.
     */
    @NotNull(message = "Location ID is required")
    private Long locationId;

    /**
     * Maximum number of teams that can participate.
     * Defaults to 0 if not specified.
     */
    @NotNull(message = "Need a maximum number of teams")
    @Min(value = 0, message = "Maximum teams cannot be negative")
    private Integer maxTeams = 0;

    /**
     * Format of the tournament (e.g., Round Robin, Single Elimination).
     */
    @NotNull(message = "Tournament format is required")
    private TournamentFormat tournamentFormat;

    /**
     * Format specifics if the tournament uses a knockout system.
     * This field is optional and can be null.
     */
    @NotNull(message = "Knockout format is required")
    private KnockoutFormat knockoutFormat;

    /**
     * List representing the prize pool. Each float can represent a prize tier.
     * This field is optional.
     */
    private List<@Positive(message = "Prize amount must be positive") Float> prizePool;

    /**
     * Minimum rank required to join the tournament.
     * This field is optional.
     */
    @Min(value = 0, message = "Minimum rank cannot be negative")
    private Integer minRank;

    /**
     * Maximum rank allowed to join the tournament.
     * This field is optional.
     */
    @Min(value = 0, message = "Maximum rank cannot be negative")
    private Integer maxRank;

    @NotNull(message = "Host is required")
    private HostProfile host;

}
