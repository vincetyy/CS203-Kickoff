package com.crashcourse.kickoff.tms.club.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubProfile {
    private Long id;
    private String name;
    private String clubDescription;
    private double elo;
    private Long captainId;
    private List<Long> players;

    public ClubProfile(Club club) {
        this.id = club.getId();
        this.name = club.getName();
        this.clubDescription = club.getClubDescription();
        this.elo = club.getElo();
        this.captainId = club.getCaptainId();
        this.players = club.getPlayers();
    }

}