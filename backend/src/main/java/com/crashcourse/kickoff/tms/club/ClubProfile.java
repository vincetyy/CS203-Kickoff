package com.crashcourse.kickoff.tms.club;

import java.util.List;
import java.util.stream.Collectors;

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
    private String captainName;
    private List<String> playerNames;

    public ClubProfile(Club club) {
        this.id = club.getId();
        this.name = club.getName();
        this.clubDescription = club.getClubDescription();
        this.elo = club.getElo();
        this.captainName = club.getCaptain().getUser().getUsername();
        this.playerNames = club.getPlayers().stream()
                .map(player -> player.getUser().getUsername())
                .collect(Collectors.toList());
    }

}