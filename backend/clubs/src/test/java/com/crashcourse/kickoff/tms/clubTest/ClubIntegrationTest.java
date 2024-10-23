package com.crashcourse.kickoff.tms.clubTest;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.ParameterizedTypeReference;

import java.util.*;

import com.crashcourse.kickoff.tms.club.repository.ClubRepository;
import com.crashcourse.kickoff.tms.club.service.ClubService;
import com.crashcourse.kickoff.tms.club.model.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ClubIntegrationTest {

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubService clubService;

    @BeforeEach
    void setUp() {
        // Create an admin user
        clubRepository.deleteAll();
        for (long i = 1; i <= 7; i++) {
			// reminder to change list of players to be populated already using array of IDs, and change creator ID
			// creators are users 1 to 7
			// players are users i+(7*k), where k is 1 to 6
			ArrayList<Long> players = new ArrayList<>();
			for (long k = 1; k < 5; k++) {
				players.add(i + (7 * k));
			}

			Club newClub = new Club((Long) i, "Club " + i, 500 + i*200, 50, (Long) i, players, "", new ArrayList<Long>());
			try {
				clubRepository.save(newClub);
				System.out.println("[Added club]: " + newClub.getName());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Couldn't create club");
			}
		}
        
    }

    @AfterEach
    void tearDown() {
        clubRepository.deleteAll();
    }

    @Test
    public void getClubs_Success() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/clubs");

        ResponseEntity<Club[]> result = restTemplate.getForEntity(uri, Club[].class);
        Club[] clubs = result.getBody();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(7, clubs.length);
    }

    @Test
    public void getPlayersFromClub_Success() throws Exception {
        Long clubId = 8L; 
        URI uri = new URI(baseUrl + port + "/api/v1/clubs/" + clubId + "/players");

        ResponseEntity<List<Long>> response = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Long>>() {}
        );

        List<Long> playerIds = response.getBody();
        
        assertNotNull(playerIds);
        assertEquals(4, playerIds.size());
    }

    @Test
    public void getPlayersFromClub_Failure() throws Exception {
        Long clubId = 10000L;
        URI uri = new URI(baseUrl + port + "/api/v1/clubs/" + clubId + "/players");

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        String error = response.getBody();
        
        assertNotNull(error);
        assertEquals("Club with ID 10000 not found", error);
    }

    @Test
    public void getClubProfile_Success() throws Exception {
        Long clubId = 28L;
        URI uri = new URI(baseUrl + port + "/api/v1/clubs/" + clubId);

        ResponseEntity<ClubProfile> response = restTemplate.getForEntity(uri, ClubProfile.class);

        ClubProfile clubProfile = response.getBody();
        
        assertNotNull(clubProfile);
        assertEquals(28L, clubProfile.getId());
    }

    @Test
    public void getClubProfile_Failure() throws Exception {
        Long clubId = 10000L;  
        URI uri = new URI(baseUrl + port + "/api/v1/clubs/" + clubId);

        ResponseEntity<ClubProfile> response = restTemplate.getForEntity(uri, ClubProfile.class);

        ClubProfile clubProfile = response.getBody();
        
        assertNotNull(clubProfile);
        assertEquals(null, clubProfile.getId());
    }
}