package com.crashcourse.kickoff.tms;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.crashcourse.kickoff.tms.club.model.Club;
import com.crashcourse.kickoff.tms.club.service.ClubService;

@SpringBootApplication
public class KickoffTournamentManagementApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(KickoffTournamentManagementApplication.class, args);

		initialiseMockData(ctx);
	}

	private static void initialiseMockData(ApplicationContext ctx) {
		ClubService clubService = ctx.getBean(ClubService.class);

		// Club
		final int NUM_MOCKED_CLUBS = 7;
		final int NUM_PLAYERS_IN_CLUB = 7;

		// random club descriptions
		String[] demoClubDescriptions = {
			"Welcome to our club! We are a group of friends who love to play football together recreationally, just a casual team. We are looking for more players to join us!",
			"Join us for a fun time playing football! We are a group of football enthusiasts who love to play the beautiful game.",
			"Looking for players to join our club! We play football every weekend and are looking for more players to join us.",
			"Bunch of friends who love to play football. We are looking for more players to join us for a fun time playing football.",
			"Soccer club looking for players to join us. We play football every weekend and are looking for more players to join us.",
			"we love footbol",
			"only advanced players, we are all ex-national team players!"
		};

		for (long i = 1; i <= NUM_MOCKED_CLUBS; i++) {
			// reminder to change list of players to be populated already using array of IDs, and change creator ID
			// creators are users 1 to 7
			// players are users i+(7*k), where k is 1 to 6
			ArrayList<Long> players = new ArrayList<>();
			for (long k = 1; k < NUM_PLAYERS_IN_CLUB; k++) {
				players.add(i + (7 * k));
			}

			Club newClub = new Club((Long) i, "Club " + i, 500 + i*200, 50, (Long) i, players, demoClubDescriptions[((int)i) - 1], new ArrayList<Long>());
			try {
				clubService.createClub(newClub, (Long) i);
				System.out.println("[Added club]: " + newClub.getName());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Couldn't create club");
			}
		}
	}
}
