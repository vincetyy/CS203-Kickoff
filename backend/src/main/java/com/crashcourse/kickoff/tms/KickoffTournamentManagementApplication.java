package com.crashcourse.kickoff.tms;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.crashcourse.kickoff.tms.user.Role;
import com.crashcourse.kickoff.tms.user.User;
import com.crashcourse.kickoff.tms.user.UserRepository;

@SpringBootApplication
public class KickoffTournamentManagementApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(KickoffTournamentManagementApplication.class, args);

		initialiseMockData(ctx);
	}

	private static Set<Role> getAllRolesAsSet() {
		return Arrays.stream(Role.values())
				.collect(Collectors.toSet());
	}

	private static void initialiseMockData(ApplicationContext ctx) {
		// JPA user repository init
		UserRepository users = ctx.getBean(UserRepository.class);
		BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
		System.out.println("[Add user]: " + users.save(
				new User("admin", encoder.encode("password"), getAllRolesAsSet())).getUsername());

	}

}
