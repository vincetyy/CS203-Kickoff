package com.crashcourse.kickoff.tms.match.model;

import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import com.crashcourse.kickoff.tms.tournament.model.Tournament;

@Entity
@NoArgsConstructor
@Data
@DiscriminatorValue("SINGLE_ELIM")
public class SingleEliminationBracket extends Bracket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "bracket_id") 
    private List<Round> rounds;
}
