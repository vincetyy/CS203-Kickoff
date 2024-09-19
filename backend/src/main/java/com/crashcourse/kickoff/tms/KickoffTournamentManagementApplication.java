package com.crashcourse.kickoff.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan(basePackages = {"com.crashcourse.kickoff.tms.tournament", "com.crashcourse.kickoff.tms.club"})
public class KickoffTournamentManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(KickoffTournamentManagementApplication.class, args);
	}

}
