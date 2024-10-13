package com.crashcourse.kickoff.tms.player;

import java.util.List;

import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.tournament.model.PlayerAvailability;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class PlayerProfile {
    
    @Id
    private Long id;

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
}
