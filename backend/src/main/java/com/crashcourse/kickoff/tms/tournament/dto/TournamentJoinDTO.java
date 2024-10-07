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
public class TournamentJoinDTO {
    
    /*
     * Club ID
     */
    @NotNull(message = "Club ID is required")
    @Min(value = 1, message = "Club ID must be greater than 0")
    private Long clubId;

    /*
     * Tournament ID
     */
    @NotNull(message = "Tournament ID is required")
    @Min(value = 1, message = "Tournament ID must be greater than 0")
    private Long tournamentId;
}
