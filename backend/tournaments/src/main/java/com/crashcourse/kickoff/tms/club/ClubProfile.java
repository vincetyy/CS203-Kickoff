package com.crashcourse.kickoff.tms.club.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubProfile {
    private Long id;
    private String name;
    private String clubDescription;
    private double elo;
    private Long captainId;
    private List<Long> players;
}