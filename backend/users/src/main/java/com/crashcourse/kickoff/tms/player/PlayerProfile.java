package com.crashcourse.kickoff.tms.player;

import java.util.List;

import com.crashcourse.kickoff.tms.user.model.User;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PlayerProfile {
    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId // Shares the primary key with User
    @JoinColumn(name = "user_id")  // The foreign key column name
    private User user;

    @ElementCollection(targetClass = PlayerPosition.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "player_profile_positions", joinColumns = @JoinColumn(name = "player_profile_id"))
    @Column(name = "preferred_position")
    private List<PlayerPosition> preferredPositions;

    private String profileDescription;
}
