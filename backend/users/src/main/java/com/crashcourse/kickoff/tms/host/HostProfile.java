package com.crashcourse.kickoff.tms.host;

import com.crashcourse.kickoff.tms.user.model.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.*;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Data
public class HostProfile {
    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId // Shares the primary key with User
    @JoinColumn(name = "user_id")  // The foreign key column name
    private User user;

}
