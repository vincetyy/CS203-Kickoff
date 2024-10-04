package com.crashcourse.kickoff.tms.club;

import jakarta.persistence.*;

import com.crashcourse.kickoff.tms.tournament.model.*;
import com.crashcourse.kickoff.tms.user.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

// note lombok's @data annotations generates getters and setters automatically
public class Club {
    public static final int MAX_PLAYERS_IN_CLUB = 20;
    public static final int MAX_CLUB_NAME_LENGTH = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Club name cannot be null")
    @Size(min = 1, max = MAX_CLUB_NAME_LENGTH, message = "Club name must be between 1 and " + MAX_CLUB_NAME_LENGTH + " characters")
    private String name;

    private double elo;
    private double ratingDeviation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private PlayerProfile captain;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @Size(max = MAX_PLAYERS_IN_CLUB, message = "A club cannot have more than " +  MAX_PLAYERS_IN_CLUB + " players")
    private List<PlayerProfile> players = new ArrayList<>();

    @ManyToMany(mappedBy = "joinedClubs")
    private List<Tournament> tournaments = new ArrayList<>();
}