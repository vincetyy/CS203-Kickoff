package com.crashcourse.kickoff.tms.tournament.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @Column(name = "player_id", nullable = false)
    private Long playerId; 

    @Column(name = "club_id", nullable = false)
    private Long clubId;  
    
    private boolean available; 
}
