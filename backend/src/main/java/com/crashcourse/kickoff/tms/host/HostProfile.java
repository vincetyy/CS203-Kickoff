package com.crashcourse.kickoff.tms.host;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.user.model.User;
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
public class HostProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "hostProfile", cascade = CascadeType.ALL)  // Corrected mappedBy field
    private User user;
}
