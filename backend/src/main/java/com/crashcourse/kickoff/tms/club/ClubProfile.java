package com.crashcourse.kickoff.tms.club;

import java.util.List;
import java.util.stream.Collectors;

import com.crashcourse.kickoff.tms.player.PlayerProfile;

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
    private PlayerProfile captain;
    private List<String> playerNames;

    public ClubProfile(Club club) {
        System.out.println(club.getCaptain());
        this.id = club.getId();
        this.name = club.getName();
        this.clubDescription = club.getClubDescription();
        this.elo = club.getElo();
        this.captain = club.getCaptain();
        this.playerNames = club.getPlayers().stream()
                .map(player -> player.getUser().getUsername())
                .collect(Collectors.toList());
    }

}