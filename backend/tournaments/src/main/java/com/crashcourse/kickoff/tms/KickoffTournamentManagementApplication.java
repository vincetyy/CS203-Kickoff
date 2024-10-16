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
import com.crashcourse.kickoff.tms.tournament.dto.PlayerAvailabilityDTO;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentJoinDTO;

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
		Location location4 = new Location(null, "Jurong East Sports Hall", null);
		locationService.createLocation(location4);
		Location location5 = new Location(null, "Bras Basah (SMU)", null);
		locationService.createLocation(location5);
		Location location6 = new Location(null, "National Stadium Courts", null);
		locationService.createLocation(location6);
		Location location7 = new Location(null, "Our Tampines Hub", null);
		locationService.createLocation(location7);
		Location location8 = new Location(null, "Woodlands Football Courts", null);
		locationService.createLocation(location8);

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
		// 136 -- tourney 1
		// 1457 -- tourney 2
		// 134567 -- tourney 3

		// create tournament join dtos for tournament1
		TournamentJoinDTO tournamentJoinDTO1 = new TournamentJoinDTO(1L, 1L);
		tournamentService.joinTournamentAsClub(tournamentJoinDTO1);
		TournamentJoinDTO tournamentJoinDTO2 = new TournamentJoinDTO(3L, 1L);
		tournamentService.joinTournamentAsClub(tournamentJoinDTO2);
		TournamentJoinDTO tournamentJoinDTO3 = new TournamentJoinDTO(6L, 1L);
		tournamentService.joinTournamentAsClub(tournamentJoinDTO3);
		
		// create tournament join dtos for tournament2
		TournamentJoinDTO tournamentJoinDTO4 = new TournamentJoinDTO(1L, 2L);
		tournamentService.joinTournamentAsClub(tournamentJoinDTO4);
		TournamentJoinDTO tournamentJoinDTO5 = new TournamentJoinDTO(7L, 2L);
		tournamentService.joinTournamentAsClub(tournamentJoinDTO5);
		TournamentJoinDTO tournamentJoinDTO6 = new TournamentJoinDTO(4L, 2L);
		tournamentService.joinTournamentAsClub(tournamentJoinDTO6);
		TournamentJoinDTO tournamentJoinDTO7 = new TournamentJoinDTO(5L, 2L);
		tournamentService.joinTournamentAsClub(tournamentJoinDTO7);

		// create tournament join dtos for tournament3
		TournamentJoinDTO tournamentJoinDTO8 = new TournamentJoinDTO(1L, 3L);
		tournamentService.joinTournamentAsClub(tournamentJoinDTO8);
		TournamentJoinDTO tournamentJoinDTO9 = new TournamentJoinDTO(3L, 3L);
		tournamentService.joinTournamentAsClub(tournamentJoinDTO9);
		TournamentJoinDTO tournamentJoinDTO10 = new TournamentJoinDTO(4L, 3L);
		tournamentService.joinTournamentAsClub(tournamentJoinDTO10);
		TournamentJoinDTO tournamentJoinDTO11 = new TournamentJoinDTO(5L, 3L);
		tournamentService.joinTournamentAsClub(tournamentJoinDTO11);
		TournamentJoinDTO tournamentJoinDTO12 = new TournamentJoinDTO(6L, 3L);
		tournamentService.joinTournamentAsClub(tournamentJoinDTO12);
		TournamentJoinDTO tournamentJoinDTO13 = new TournamentJoinDTO(7L, 3L);
		tournamentService.joinTournamentAsClub(tournamentJoinDTO13);

		// add users avail for tournament, esp users 8, 15, 22, 29.
		// users 36, 43 not avail, user50 will be the demo user to apply
		// in my demo: they're all part of club 2
		for (long i = 1; i <= 29; i+=7) {
			PlayerAvailabilityDTO playerAvailabilityDTO = new PlayerAvailabilityDTO(1L, i, 2L, true);
			tournamentService.updatePlayerAvailability(playerAvailabilityDTO);
		}

		for (long i = 36; i <= 43; i+=7) {
			PlayerAvailabilityDTO playerAvailabilityDTO = new PlayerAvailabilityDTO(1L, i, 2L, false);
			tournamentService.updatePlayerAvailability(playerAvailabilityDTO);
		}
	}
}
