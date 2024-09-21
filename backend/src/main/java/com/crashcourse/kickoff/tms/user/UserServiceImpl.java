package com.crashcourse.kickoff.tms.user;

import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User addUser(UserDTO newUserDTO) {
        User newUser = new User();
        newUser.setUsername(newUserDTO.getUsername());
        newUser.setPassword(encoder.encode(newUserDTO.getPassword()));
        // defaults new users to "user" role
        newUser.setRoles(Set.of(Role.ROLE_USER));
        return users.save(newUser);
    }
}
