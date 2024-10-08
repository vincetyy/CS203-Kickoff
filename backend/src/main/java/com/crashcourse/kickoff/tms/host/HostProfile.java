package com.crashcourse.kickoff.tms.host;

import com.crashcourse.kickoff.tms.user.model.User;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class HostProfile {
    @Id
    private Long id;

    @OneToOne
    @MapsId // Shares the primary key with User
    @JoinColumn(name = "user_id")  // The foreign key column name
    private User user;
}
