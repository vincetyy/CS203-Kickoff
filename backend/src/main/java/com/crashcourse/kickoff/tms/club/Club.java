package com.crashcourse.kickoff.tms.club;

import javax.persistence.*;

import com.crashcourse.kickoff.tms.tournament.*;
import com.crashcourse.kickoff.tms.user.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Size(min = 1, max = MAX_CLUB_NAME_LENGTH, message = String.format("Club name must be 1 to %d characters", MAX_CLUB_NAME_LENGTH))
    private String name;

    private double elo;
    private double ratingDeviation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User captain;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @Size(max = MAX_PLAYERS_IN_CLUB, message = String.format("A club cannot have more than %d players", MAX_PLAYERS_IN_CLUB))
    private List<User> players = new ArrayList<>();

    @ManyToMany(mappedBy = "joinedClubs")
    private List<Tournament> tournaments = new ArrayList<>();
}