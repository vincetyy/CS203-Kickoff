package com.crashcourse.kickoff.tms.match.model;

import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import com.crashcourse.kickoff.tms.tournament.model.Tournament;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
@DiscriminatorValue("SINGLE_ELIM")
public class SingleEliminationBracket extends Bracket {

    /*
     * Primary key (@Id) should only be defined in the base class (Bracket)
     * Each subclass will inherit this primary key
     */

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "bracket_id") 
    private List<Round> rounds;
}
