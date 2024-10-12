package com.crashcourse.kickoff.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.club.ClubService;
import com.crashcourse.kickoff.tms.location.model.Location;
import com.crashcourse.kickoff.tms.location.repository.LocationRepository;
import com.crashcourse.kickoff.tms.location.service.LocationService;
import com.crashcourse.kickoff.tms.security.SecurityConfig;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentCreateDTO;
import com.crashcourse.kickoff.tms.tournament.model.KnockoutFormat;
import com.crashcourse.kickoff.tms.tournament.model.Tournament;
import com.crashcourse.kickoff.tms.tournament.model.TournamentFormat;
import com.crashcourse.kickoff.tms.tournament.service.TournamentService;


import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
public class KickoffTournamentManagementApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(KickoffTournamentManagementApplication.class, args);

		initialiseMockData(ctx);
	}

	private static void initialiseMockData(ApplicationContext ctx) {
		// Club
		ClubService clubService = ctx.getBean(ClubService.class);
		Club newClub = new Club(null, "My New Club", 500, 50, 1L, new ArrayList<Long>(), new ArrayList<Tournament>(), "Club DESCRIPTION", new ArrayList<Long>());
		
		try {
			clubService.createClub(newClub, 1L);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Gone case");
		}
		System.out.println("[Add club]: " + newClub.getName());


		// Location
		LocationService locationService = ctx.getBean(LocationService.class);
		Location location1 = new Location(null, "MBS", null);
		locationService.createLocation(location1);
		Location location2 = new Location(null, "Botanic Gardens", null);
		locationService.createLocation(location2);

		// Tournament
		TournamentService tournamentService = ctx.getBean(TournamentService.class);
		TournamentCreateDTO tournament1DTO = new TournamentCreateDTO("Tournament 1", LocalDateTime.of(
            2021, 4, 24, 14, 33, 48), LocalDateTime.of(
			2021,5, 24, 14, 33, 48), location1, 16, TournamentFormat.FIVE_SIDE, KnockoutFormat.SINGLE_ELIM, new ArrayList<Float>(), null, null);
		tournamentService.createTournament(tournament1DTO, 1L);
		
	}
}
