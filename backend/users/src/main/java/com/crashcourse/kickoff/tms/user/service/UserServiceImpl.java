package com.crashcourse.kickoff.tms.user.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.crashcourse.kickoff.tms.host.service.HostProfileService;
import com.crashcourse.kickoff.tms.player.service.PlayerProfileService;
import com.crashcourse.kickoff.tms.user.UserRepository;
import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.Role;
import com.crashcourse.kickoff.tms.user.model.User;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository users;
    private HostProfileService hostProfileService;
    private PlayerProfileService playerProfileService;
    private BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository users, HostProfileService hostProfileService,
            PlayerProfileService playerProfileService, BCryptPasswordEncoder encoder) {
        this.users = users;
        this.hostProfileService = hostProfileService;
        this.playerProfileService = playerProfileService;
        this.encoder = encoder;
    }

    public List<User> getUsers() {
        return users.findAll();
    }

    @Transactional
    @Override
    public User addUser(NewUserDTO newUserDTO) {
        User newUser = new User();
        newUser.setUsername(newUserDTO.getUsername());
        newUser.setPassword(encoder.encode(newUserDTO.getPassword()));
        newUser.setEmail(newUserDTO.getEmail());
        
        Role newUserRole;
        try {
            newUserRole = Role.valueOf("ROLE_" + newUserDTO.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + newUserDTO.getRole());
        }
        newUser.setRoles(new HashSet<Role>(Arrays.asList(newUserRole)));

        newUser = users.save(newUser);
        // Build the entire object graph before saving
        switch (newUserRole) {
            case Role.ROLE_PLAYER:
                playerProfileService.addPlayerProfile(newUser, newUserDTO);
                break;
            case Role.ROLE_HOST:
                hostProfileService.addHostProfile(newUser);
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + newUserDTO.getRole());
        }

        // Save the user along with the associated profile
        return users.save(newUser);
    }

    @Transactional
    @Override
    public User loadUserByUsername(String userName) {
        return users.findByUsername(userName).isPresent() ? users.findByUsername(userName).get() : null;
    }

    @Transactional
    @Override
    public User getUserById(Long userId) {
        return users.findById(userId).orElse(null);  
    }

    @Transactional
    public User save(User user) {
        return users.save(user);  // Save the user and persist changes to the database
    }

}
