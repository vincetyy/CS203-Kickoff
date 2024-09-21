
import javax.persistence.*;

import com.crashcourse.kickoff.tms.tournament.Tournament;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double elo;
    private double ratingDeviation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User captain; // Player or User?? also do i need to annotate this @ManyToOne?

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<User> players = new ArrayList<>(); // Player or User?? -- i think if user is only player or host, we can just use player

    // @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    // private List<Tournament> pastTournaments = new ArrayList<>();  // @zane: we need a boolean value that's changed in tournament side when a tournament is over, so logic is easy there

    // @ManyToMany(mappedBy = "joinedClubs")
    // private List<Tournament> upcomingTournaments = new ArrayList<>();

    @ManyToMany(mappedBy = "joinedClubs")
    private List<Tournament> tournaments = new ArrayList<>(); // tournament side will handle match history
}