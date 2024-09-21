package com.crashcourse.kickoff.tms.user;

import java.util.List;

// Business logic of UserService, actual implementation in UserServiceImpl
public interface UserService {
    public List<User> getUsers();
    User addUser(UserDTO newUserDTO);
}
