import com.example.tournamentmanagement.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    // add query methods (remember that each method's implementation is through the method name ah set lin)
}