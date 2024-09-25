package com.crashcourse.kickoff.tms.location.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    public Location() {
    }

}
