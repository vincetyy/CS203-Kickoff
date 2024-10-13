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
		// Club
		ClubService clubService = ctx.getBean(ClubService.class);
		Club newClub = new Club(null, "My New Club", 500, 50, 1L, new ArrayList<Long>(), "Club DESCRIPTION", new ArrayList<Long>());
		
		try {
			clubService.createClub(newClub, 1L);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Gone case");
		}
		System.out.println("[Add club]: " + newClub.getName());
	}
}
