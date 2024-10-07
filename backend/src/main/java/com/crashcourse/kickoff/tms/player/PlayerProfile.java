package com.crashcourse.kickoff.tms.player;

import java.util.List;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    
    @ElementCollection(targetClass = PlayerPosition.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "player_profile_positions", joinColumns = @JoinColumn(name = "player_profile_id"))
    @Column(name = "preferred_position")
    private List<PlayerPosition> preferredPositions;

    private String profileDescription;

    public boolean isFreeAgent() {
        return club == null;
    }
}
