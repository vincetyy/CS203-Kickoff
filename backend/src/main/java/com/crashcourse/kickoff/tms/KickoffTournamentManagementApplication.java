package com.crashcourse.kickoff.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.crashcourse.kickoff.tms.security.SecurityConfig;
import com.crashcourse.kickoff.tms.user.UserRepository;
import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.user.service.UserService;
import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;

@SpringBootApplication
public class KickoffTournamentManagementApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(KickoffTournamentManagementApplication.class, args);

		initialiseMockData(ctx);
	}

	private static void initialiseMockData(ApplicationContext ctx) {
		// JPA user repository init
		UserRepository users = ctx.getBean(UserRepository.class);
		BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
		System.out.println("[Add admin]: " + users.save(
				new User("admin", encoder.encode("password"), SecurityConfig.getAllRolesAsSet())).getUsername());
		UserService userService = ctx.getBean(UserService.class);
		NewUserDTO dummyUserDTO = new NewUserDTO("dummyUser", "user@email.com", "password",
				new String[] { "POSITION_Goalkeeper", "POSITION_Midfielder" }, "player");
		System.out.println("[Add dummy user]: " + userService.addUser(dummyUserDTO).getUsername());

	}
}
