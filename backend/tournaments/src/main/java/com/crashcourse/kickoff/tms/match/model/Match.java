package com.crashcourse.kickoff.tms.match.model;

import java.util.*;

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

    private Long tournamentId;
    
    /*
     * Matches are stored in a binary tree data structure
     */

    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "left_child_id")
    private Match leftChild;


    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "right_child_id")
    private Match rightChild;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Match parentMatch;

    /*
     * Clubs
     */
    private Long club1Id;
    private Long club2Id;

    /*
     * Score
     */
    int club1Score;
    int club2Score;

    Long winningClubId;

}
