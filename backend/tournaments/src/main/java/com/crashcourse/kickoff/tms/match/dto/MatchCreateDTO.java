package com.crashcourse.kickoff.tms.match.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for responding with Tournament data.
 * This DTO excludes sensitive or unnecessary fields and presents data in a client-friendly format.
 */
@Data
@AllArgsConstructor
public class MatchCreateDTO {
    private Long tournamentId;
    private Long parentId;
}
