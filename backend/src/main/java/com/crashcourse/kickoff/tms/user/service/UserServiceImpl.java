package com.crashcourse.kickoff.tms.user.service;

import java.util.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.PlayerPosition;
import com.crashcourse.kickoff.tms.user.model.PlayerProfile;
import com.crashcourse.kickoff.tms.user.model.Role;
import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository users;
    private BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository users, BCryptPasswordEncoder encoder) {
        this.users = users;
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
        // defaults new users to "user" role
        newUser.setRoles(Set.of(Role.ROLE_USER));
        return users.save(newUser);
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

    @Override
    public PlayerProfile updatePlayerPosition(Long userId, PlayerPosition preferredPosition) {
        Optional<User> userOpt = users.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            PlayerProfile playerProfile = user.getPlayerProfile();
            if (playerProfile != null) {
                playerProfile.setPreferredPosition(preferredPosition);
                users.save(user);  // Save the user and the updated profile
                return playerProfile;
            } else {
                throw new IllegalArgumentException("Player profile not found for user with id " + userId);
            }
        } else {
            throw new IllegalArgumentException("User not found with id " + userId);
        }
    }
}
