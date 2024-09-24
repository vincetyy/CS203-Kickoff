package com.crashcourse.kickoff.tms.user.model;

import com.crashcourse.kickoff.tms.club.Club;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PlayerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="club_id")
    private Club club;


    // prevents cyclical dependency
    // i don't know if this is the correct way to do it someone pls think this through
    @JsonIgnore
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
