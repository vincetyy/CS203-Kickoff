package com.crashcourse.kickoff.tms.location.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.*;

import com.crashcourse.kickoff.tms.tournament.model.Tournament;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@AllArgsConstructor
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Tournament> tournaments;

    public Location() {
    }

}
