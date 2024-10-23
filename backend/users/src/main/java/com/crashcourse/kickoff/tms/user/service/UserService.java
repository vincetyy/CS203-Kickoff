package com.crashcourse.kickoff.tms.user.service;

import java.util.List;

import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;

// Business logic of UserService, actual implementation in UserServiceImpl
public interface UserService {
    public List<User> getUsers();
    public User loadUserByUsername(String userName);
    User addUser(NewUserDTO newUserDTO);
    User getUserById(Long userId);   
    User save(User user);
    User addHostProfileToUser(User user);
    void deleteUserById(Long userId);
}
