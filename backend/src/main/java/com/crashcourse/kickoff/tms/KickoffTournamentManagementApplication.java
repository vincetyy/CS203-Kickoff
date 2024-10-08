package com.crashcourse.kickoff.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.crashcourse.kickoff.tms.club.Club;
import com.crashcourse.kickoff.tms.club.ClubService;
import com.crashcourse.kickoff.tms.host.HostProfile;
import com.crashcourse.kickoff.tms.host.HostProfileService;
import com.crashcourse.kickoff.tms.location.model.Location;
import com.crashcourse.kickoff.tms.location.repository.LocationRepository;
import com.crashcourse.kickoff.tms.location.service.LocationService;
import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.player.service.PlayerProfileService;
import com.crashcourse.kickoff.tms.security.SecurityConfig;
import com.crashcourse.kickoff.tms.tournament.dto.TournamentCreateDTO;
import com.crashcourse.kickoff.tms.tournament.model.KnockoutFormat;
import com.crashcourse.kickoff.tms.tournament.model.Tournament;
import com.crashcourse.kickoff.tms.tournament.model.TournamentFormat;
import com.crashcourse.kickoff.tms.tournament.service.TournamentService;
import com.crashcourse.kickoff.tms.user.UserRepository;
import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.user.service.UserService;
import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;

import java.time.LocalDateTime;
import java.util.*;

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
		admin = userService.getUserById(admin.getId());
		HostProfile adminHostProfile = hostProfileService.addHostProfile(admin);
		System.out.println("[Add admin]: " + admin.getUsername());

		// Creating dummy
		NewUserDTO dummyUserDTO = new NewUserDTO("dummyUser", "user@email.com", "password",
				new String[] { "POSITION_Goalkeeper", "POSITION_Midfielder" }, "player");
		User dummy = userService.addUser(dummyUserDTO);
		System.out.println("[Add dummy user]: " + dummy.getUsername());

		// Club
		ClubService clubService = ctx.getBean(ClubService.class);
		Club newClub = new Club(null, "My New Club", 500, 50, playerProfileService.getPlayerProfile(admin.getId()), new ArrayList<PlayerProfile>(), new ArrayList<Tournament>());
		
		try {
			clubService.createClub(newClub, admin.getId());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Gone case");
		}
		System.out.println("[Add club]: " + newClub.getName());


		// Location
		LocationService locationService = ctx.getBean(LocationService.class);
		Location location1 = new Location(null, "MBS");
		locationService.createLocation(location1);
		Location location2 = new Location(null, "Botanic Gardens");
		locationService.createLocation(location2);

		// Tournament
		TournamentService tournamentService = ctx.getBean(TournamentService.class);
		TournamentCreateDTO tournament1DTO = new TournamentCreateDTO("Tournament 1", LocalDateTime.of(
            2021, 4, 24, 14, 33, 48), LocalDateTime.of(
			2021,5, 24, 14, 33, 48), location1.getId(), 16, TournamentFormat.FIVE_SIDE, KnockoutFormat.SINGLE_ELIM, new ArrayList<Float>(), null, null, adminHostProfile);
		tournamentService.createTournament(tournament1DTO);
		
	}
}
