package com.crashcourse.kickoff.tms;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.crashcourse.kickoff.tms.location.model.Location;
import com.crashcourse.kickoff.tms.location.service.LocationService;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentCreateDTO;
import com.crashcourse.kickoff.tms.tournament.model.KnockoutFormat;
import com.crashcourse.kickoff.tms.tournament.model.TournamentFormat;
import com.crashcourse.kickoff.tms.tournament.service.TournamentService;

@SpringBootApplication
public class KickoffTournamentManagementApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(KickoffTournamentManagementApplication.class, args);

		initialiseMockData(ctx);
	}

	private static void initialiseMockData(ApplicationContext ctx) {
		// Location
		LocationService locationService = ctx.getBean(LocationService.class);
		Location location1 = new Location(null, "MBS", null);
		locationService.createLocation(location1);
		Location location2 = new Location(null, "Botanic Gardens", null);
		locationService.createLocation(location2);
		Location location3 = new Location(null, "SMU", null);
		locationService.createLocation(location3);
		Location location4 = new Location(null, "Fort Canning", null);
		locationService.createLocation(location4);
		Location location5 = new Location(null, "Istana", null);
		locationService.createLocation(location5);
		

		// Tournament
		TournamentService tournamentService = ctx.getBean(TournamentService.class);
		TournamentCreateDTO tournament1DTO = new TournamentCreateDTO("Tournament 1", LocalDateTime.of(
            2021, 4, 24, 14, 33, 48), LocalDateTime.of(
			2021,5, 24, 14, 33, 48), location1, 16, TournamentFormat.FIVE_SIDE, KnockoutFormat.SINGLE_ELIM, new ArrayList<Float>(), null, null);
		tournamentService.createTournament(tournament1DTO, 1L);
		
	}
}
