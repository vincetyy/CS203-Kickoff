package com.crashcourse.kickoff.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.crashcourse.kickoff.tms.host.HostProfile;
import com.crashcourse.kickoff.tms.host.HostProfileService;
import com.crashcourse.kickoff.tms.player.service.PlayerProfileService;
import com.crashcourse.kickoff.tms.security.SecurityConfig;
import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.user.service.UserService;

@SpringBootApplication
public class KickoffTournamentManagementApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(KickoffTournamentManagementApplication.class, args);

		initialiseMockData(ctx);
	}

	private static void initialiseMockData(ApplicationContext ctx) {
		// User
		UserService userService = ctx.getBean(UserService.class);
		PlayerProfileService playerProfileService = ctx.getBean(PlayerProfileService.class);
		HostProfileService hostProfileService = ctx.getBean(HostProfileService.class);
		BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);

		// Creating admin
		NewUserDTO adminDTO = new NewUserDTO("admin", "admin@email.com", "password",
				new String[] { "POSITION_Goalkeeper", "POSITION_Midfielder" }, "player");
		User admin = userService.addUser(adminDTO);
		admin.setRoles(SecurityConfig.getAllRolesAsSet());
		admin = userService.save(admin);
		HostProfile adminHostProfile = hostProfileService.addHostProfile(admin);
		System.out.println("[Add admin]: " + admin.getUsername());

		// Creating dummy
		NewUserDTO dummyUserDTO = new NewUserDTO("dummyUser", "user@email.com", "password",
				new String[] { "POSITION_Goalkeeper", "POSITION_Midfielder" }, "player");
		User dummy = userService.addUser(dummyUserDTO);
		System.out.println("[Add dummy user]: " + dummy.getUsername());
	}
}
