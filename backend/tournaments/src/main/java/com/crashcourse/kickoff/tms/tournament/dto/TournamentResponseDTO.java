package com.crashcourse.kickoff.tms.tournament.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.crashcourse.kickoff.tms.match.model.Round;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Data Transfer Object for responding with Tournament data.
 * This DTO excludes sensitive or unnecessary fields and presents data in a client-friendly format.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentResponseDTO {

    private Long id;
    private String name;

    private boolean isOver;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocationDTO location;

    private int maxTeams;
    private String tournamentFormat;
    private String knockoutFormat;
    private List<Float> prizePool;

    private Integer minRank;
    private Integer maxRank;

    private List<Long> joinedClubsIds;
    private Long host;

    /**
     * Inner DTO for Location data.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationDTO {
        private Long id;
        private String name;
    }

    private List<Round> rounds;
}
