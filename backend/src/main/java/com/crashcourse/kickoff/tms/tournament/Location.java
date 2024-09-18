package com.crashcourse.kickoff.tms.tournament;

import lombok.*;
import jakarta.persistence.*;

@Entity
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    public Location() {
    }

}
