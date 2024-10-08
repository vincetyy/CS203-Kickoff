package com.crashcourse.kickoff.tms.player;

import java.util.List;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.user.model.User;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToOne
    @JoinColumn(name = "club_captain_of_id")
    private Club clubCaptainOf;

    @OneToOne
    @MapsId // Shares the primary key with User
    @JoinColumn(name = "id")  // The foreign key column name
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
