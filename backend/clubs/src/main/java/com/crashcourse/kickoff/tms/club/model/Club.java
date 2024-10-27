package com.crashcourse.kickoff.tms.club.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
// note lombok's @data annotations generates getters and setters automatically
public class Club {
    public static final int MAX_PLAYERS_IN_CLUB = 20;
    public static final int MAX_CLUB_NAME_LENGTH = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Club name cannot be null")
    @Size(min = 1, max = MAX_CLUB_NAME_LENGTH, message = "Club name must be between 1 and " + MAX_CLUB_NAME_LENGTH + " characters")
    private String name;

    private double elo;
    private double ratingDeviation;

    private Long captainId;
    
    @ElementCollection
    private List<Long> players = new ArrayList<>();

    private String clubDescription;

    @ElementCollection
    private List<Long> applicants = new ArrayList<>();
}