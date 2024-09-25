package com.crashcourse.kickoff.tms.tournament.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    private List<ClubDTO> joinedClubs;

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

    /**
     * Inner DTO for Club data.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubDTO {
        private Long id;
        private String name;
    }
}
