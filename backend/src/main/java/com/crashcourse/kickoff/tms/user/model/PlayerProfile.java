package com.crashcourse.kickoff.tms.user.model;

import com.crashcourse.kickoff.tms.club.Club;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class PlayerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name="club_id")
    private Club club;

    // prevents cyclical dependency
    // i don't know if this is the correct way to do it someone pls think this through
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "playerProfile", cascade = CascadeType.ALL)
    private User user;
    
    // storing one PlayerPosition for now, may change later
    // Store enum as a string in the database
    @Enumerated(EnumType.STRING)
    private PlayerPosition preferredPosition;

    private String profileDescription;

    public boolean isFreeAgent() {
        return club == null;
    }
}
