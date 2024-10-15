package com.crashcourse.kickoff.tms;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.crashcourse.kickoff.tms.location.model.Location;
import com.crashcourse.kickoff.tms.location.service.LocationService;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentCreateDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentResponseDTO;
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
		Location location3 = new Location(null, "East Coast Park", null);
		locationService.createLocation(location3);

		// Tournament
		final int NUM_MOCKED_TOURNAMENTS = 3;

		TournamentService tournamentService = ctx.getBean(TournamentService.class);
		TournamentCreateDTO tournament1DTO = new TournamentCreateDTO("Saturday East-side Tournament", LocalDateTime.of(
            2024, 10, 19, 10, 00, 00), LocalDateTime.of(
			2024, 10, 19, 18, 00, 00), location3, 8, TournamentFormat.FIVE_SIDE, KnockoutFormat.SINGLE_ELIM, new ArrayList<Float>(), 500, 2000);
		tournamentService.createTournament(tournament1DTO, 1L);
		System.out.println("[Added tournament 1]");

		TournamentCreateDTO tournament2DTO = new TournamentCreateDTO("Sunday Night Mini-Tournament", LocalDateTime.of(
            2024, 10, 20, 19, 00, 00), LocalDateTime.of(
			2024, 10, 20, 23, 00, 00), location2, 4, TournamentFormat.FIVE_SIDE, KnockoutFormat.DOUBLE_ELIM, new ArrayList<Float>(), 500, 2000);
		tournamentService.createTournament(tournament2DTO, 2L);
		System.out.println("[Added tournament 2]");


		TournamentCreateDTO tournament3DTO = new TournamentCreateDTO("Casual Tournament @ Central Singapore", LocalDateTime.of(
            2024, 10, 26, 8, 00, 00), LocalDateTime.of(
			2024, 10, 26, 13, 00, 00), location1, 16, TournamentFormat.FIVE_SIDE, KnockoutFormat.DOUBLE_ELIM, new ArrayList<Float>(), 500, 2000);
		tournamentService.createTournament(tournament3DTO, 3L);
		System.out.println("[Added tournament 3]");

		TournamentResponseDTO tournament1 = tournamentService.getTournamentById(1L);
		TournamentResponseDTO tournament2 = tournamentService.getTournamentById(2L);
		TournamentResponseDTO tournament3 = tournamentService.getTournamentById(3L);

		// im randomly (but not randomly, so its deterministic) adding clubs to tournaments
		// 236
		tournament1.getJoinedClubsIds().add(2L);
		tournament1.getJoinedClubsIds().add(3L);
		tournament1.getJoinedClubsIds().add(6L);

		// 2457
		tournament2.getJoinedClubsIds().add(2L);
		tournament2.getJoinedClubsIds().add(7L);
		tournament2.getJoinedClubsIds().add(4L);
		tournament2.getJoinedClubsIds().add(5L);

		// 234567
		tournament3.getJoinedClubsIds().add(2L);
		tournament3.getJoinedClubsIds().add(3L);
		tournament3.getJoinedClubsIds().add(4L);
		tournament3.getJoinedClubsIds().add(5L);
		tournament3.getJoinedClubsIds().add(6L);
		tournament3.getJoinedClubsIds().add(7L);


		// add users avail for tournament, esp users 8, 15, 22, 29.
		// users 36, 43 not avail, user50 will be the demo user to apply
		tournament1.updatePlayerAvailability(8L, true);
		tournament1.updatePlayerAvailability(15L, true);
		tournament1.updatePlayerAvailability(22L, true);
		tournament1.updatePlayerAvailability(29L, true);
		tournament1.updatePlayerAvailability(36L, false);
		tournament1.updatePlayerAvailability(43L, false);

		
	}
}
