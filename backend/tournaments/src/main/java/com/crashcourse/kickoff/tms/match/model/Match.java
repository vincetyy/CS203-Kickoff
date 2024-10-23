package com.crashcourse.kickoff.tms.match.model;

import java.util.*;

import com.crashcourse.kickoff.tms.tournament.model.Tournament;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isOver;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    /*
     * Clubs
     */
    private Long club1Id;
    private Long club2Id;

    /*
     * Score
     */
    private int club1Score;
    private int club2Score;

    private Long winningClubId;

}
