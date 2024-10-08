package com.crashcourse.kickoff.tms.club;

import java.util.ArrayList;
import java.util.List;

import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.tournament.model.Tournament;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")

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

    @OneToOne(mappedBy = "clubCaptainOf", cascade = CascadeType.PERSIST)
    private PlayerProfile captain;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @Size(max = MAX_PLAYERS_IN_CLUB, message = "A club cannot have more than " +  MAX_PLAYERS_IN_CLUB + " players")
    private List<PlayerProfile> players = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "joinedClubs")
    private List<Tournament> tournaments = new ArrayList<>();

    private String clubDescription;

    @ManyToMany
    @JoinTable(
        name = "club_applicants",
        joinColumns = @JoinColumn(name = "club_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<PlayerProfile> applicants = new ArrayList<>();
}