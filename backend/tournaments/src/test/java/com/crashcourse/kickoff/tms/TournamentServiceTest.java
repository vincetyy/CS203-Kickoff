// // Necessary imports
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.BeforeEach;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import java.time.LocalDateTime;
// import java.util.*;

// import com.crashcourse.kickoff.tms.tournament.dto.*;
// import com.crashcourse.kickoff.tms.tournament.dto.TournamentResponseDTO.LocationDTO;
// import com.crashcourse.kickoff.tms.tournament.model.*;
// import com.crashcourse.kickoff.tms.tournament.repository.*;
// import com.crashcourse.kickoff.tms.tournament.service.TournamentServiceImpl;
// import com.crashcourse.kickoff.tms.location.model.Location;
// import com.crashcourse.kickoff.tms.location.repository.LocationRepository;
// import com.crashcourse.kickoff.tms.location.service.LocationService;
// import jakarta.persistence.EntityNotFoundException;
// import org.springframework.web.client.RestTemplate;

// public class TournamentServiceTest {

//     @Mock
//     private TournamentRepository tournamentRepository;

//     @Mock
//     private LocationRepository locationRepository;

//     @Mock
//     private LocationService locationService;

//     @Mock
//     private PlayerAvailabilityRepository playerAvailabilityRepository;

//     @Mock
//     private RestTemplate restTemplate;

//     @InjectMocks
//     private TournamentServiceImpl tournamentService;

//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.openMocks(this);
//     }

//     // ============= createtournament   =================
//     @Test
//     public void createTournament_ValidData_TournamentCreatedSuccessfully() {
//         // Arrange
//         Long userIdFromToken = 1L;
//         Long locationId = 100L;
    
//         Location location = new Location();
//         location.setId(locationId);
//         location.setName("Stadium");
    
//         TournamentCreateDTO dto = new TournamentCreateDTO();
//         dto.setName("Champions League");
//         dto.setStartDateTime(LocalDateTime.now().plusDays(1));
//         dto.setEndDateTime(LocalDateTime.now().plusDays(2));
//         dto.setLocation(location);
//         dto.setMaxTeams(16);
//         dto.setTournamentFormat(TournamentFormat.FIVE_SIDE);
//         dto.setKnockoutFormat(KnockoutFormat.SINGLE_ELIM);
//         dto.setPrizePool(Arrays.asList(1000.0f)); 
//         dto.setMinRank(1);
//         dto.setMaxRank(100);
    
//         Tournament savedTournament = new Tournament();
//         savedTournament.setId(10L);
//         savedTournament.setName(dto.getName());
//         savedTournament.setStartDateTime(dto.getStartDateTime());
//         savedTournament.setEndDateTime(dto.getEndDateTime());
//         savedTournament.setLocation(location);
//         savedTournament.setMaxTeams(dto.getMaxTeams());
//         savedTournament.setTournamentFormat(dto.getTournamentFormat());
//         savedTournament.setKnockoutFormat(dto.getKnockoutFormat());
//         savedTournament.setPrizePool(dto.getPrizePool());
//         savedTournament.setMinRank(dto.getMinRank());
//         savedTournament.setMaxRank(dto.getMaxRank());
//         savedTournament.setHost(userIdFromToken);
//         savedTournament.setJoinedClubIds(new ArrayList<>());
    
//         when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
//         when(tournamentRepository.save(any(Tournament.class))).thenReturn(savedTournament);
    
//         // Act
//         TournamentResponseDTO result = null;
//         try {
//             result = tournamentService.createTournament(dto, userIdFromToken);
//         } catch (Exception e) {
//             fail("Exception should not be thrown");
//         }
    
//         // Assert
//         assertNotNull(result);
//         assertEquals(savedTournament.getId(), result.getId());
//         assertEquals(savedTournament.getName(), result.getName());
//         assertEquals(savedTournament.getHost(), result.getHost());
//         verify(locationRepository, times(1)).findById(locationId);
//         verify(tournamentRepository, times(1)).save(any(Tournament.class));
//     }

// }