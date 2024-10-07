package com.crashcourse.kickoff.tms.user.service;

import java.util.List;

import com.crashcourse.kickoff.tms.player.PlayerPosition;
import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.user.dto.LoginDetails;
import com.crashcourse.kickoff.tms.user.model.User;

// Business logic of UserService, actual implementation in UserServiceImpl
public interface UserService {
    public List<User> getUsers();
    public User loadUserByUsername(String userName);
    User addUser(LoginDetails newUserDTO);
    //PlayerProfile addPlayerProfile(Long userId, PlayerProfile playerProfile);
}
