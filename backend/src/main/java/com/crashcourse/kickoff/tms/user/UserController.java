package com.crashcourse.kickoff.tms.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crashcourse.kickoff.tms.player.PlayerPosition;
import com.crashcourse.kickoff.tms.player.PlayerProfile;
import com.crashcourse.kickoff.tms.player.dto.PlayerPositionDTO;
import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Using BCrypt encoder to encrypt the password for storage
     * 
     * @param user
     * @return
     */
    @PostMapping
    public User addUser(@Valid @RequestBody NewUserDTO newUserDTO) {
        return userService.addUser(newUserDTO);
    }

    @PostMapping("/{userId}/playerProfile")
    public ResponseEntity<PlayerProfile> addPlayerProfile(@PathVariable Long userId, @RequestBody PlayerProfile profile) {
        PlayerProfile createdProfile = userService.addPlayerProfile(userId, profile);
        return createdProfile != null ? ResponseEntity.ok(createdProfile) : ResponseEntity.notFound().build();
    }

    // pls check this
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody NewUserDTO newUserDTO) {
        try {
            User newUser = userService.addUser(newUserDTO); // call the addUser method in the service
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();  
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
