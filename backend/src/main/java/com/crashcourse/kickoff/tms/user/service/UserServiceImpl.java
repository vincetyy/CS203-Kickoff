package com.crashcourse.kickoff.tms.user.service;

import java.util.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.crashcourse.kickoff.tms.host.HostProfile;
import com.crashcourse.kickoff.tms.host.HostProfileRepository;
import com.crashcourse.kickoff.tms.player.PlayerPosition;
import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.player.respository.PlayerProfileRepository;
import com.crashcourse.kickoff.tms.user.UserRepository;
import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.Role;
import com.crashcourse.kickoff.tms.user.model.User;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository users;
    private HostProfileRepository hostProfileRepository;
    private PlayerProfileRepository playerProfileRepository;
    private BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository users, HostProfileRepository hostProfileRepository, PlayerProfileRepository playerProfileRepository, BCryptPasswordEncoder encoder) {
        this.users = users;
        this.hostProfileRepository = hostProfileRepository;
        this.playerProfileRepository = playerProfileRepository;
        this.encoder = encoder;
    }


    public List<User> getUsers() {
        return users.findAll();
    }
    @Override
    public User addUser(NewUserDTO newUserDTO) {
        User newUser = new User();
        newUser.setUsername(newUserDTO.getUsername());
        newUser.setPassword(encoder.encode(newUserDTO.getPassword()));
        newUser.setRoles(Set.of(Role.ROLE_USER)); // Set default role

        // Save the user first to get the user ID
        User savedUser = users.save(newUser);

        // Check the role and create the respective profile
        if (newUserDTO.getRole().equals(Role.ROLE_PLAYER.name())) {
            PlayerProfile playerProfile = new PlayerProfile();
            playerProfile.setUser(savedUser); // Bidirectional relationship
            savedUser.setPlayerProfile(playerProfile); // Link profile to user
            playerProfileRepository.save(playerProfile); // Save player profile
        } else if (newUserDTO.getRole().equals(Role.ROLE_HOST.name())) {
            HostProfile hostProfile = new HostProfile();
            hostProfile.setUser(savedUser); // Bidirectional relationship
            savedUser.setHostProfile(hostProfile); // Link profile to user
            hostProfileRepository.save(hostProfile); // Save host profile
        }

        // Return the saved user with the linked profile
        return users.save(savedUser); // Save the user again to update the relationship
    }

    @Override
    public PlayerProfile addPlayerProfile(Long userId, PlayerProfile playerProfile) {
        Optional<User> userOpt = users.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPlayerProfile(playerProfile);
            playerProfile.setUser(user); // Maintain bidirectional relationship
            users.save(user);  // Save the user to update profile in DB
            return playerProfile;
        }
        return null;
    }
}
