package com.crashcourse.kickoff.tms.match.model;

import java.util.*;

import com.crashcourse.kickoff.tms.tournament.model.Tournament;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<Match> matches;
}
