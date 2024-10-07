package com.crashcourse.kickoff.tms.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crashcourse.kickoff.tms.security.JwtUtil;
import com.crashcourse.kickoff.tms.user.dto.LoginDetails;
import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

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
    public User addUser(@Valid @RequestBody LoginDetails newUserDTO) {
        return userService.addUser(newUserDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDetails loginDetails) throws Exception {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDetails.getUsername(), loginDetails.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid username or password", e);
        }

        // Load user details and generate JWT token
        final User userDetails = userService.loadUserByUsername(loginDetails.getUsername());
        System.out.println(loginDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwt);
    }

    // @PostMapping("/{userId}/playerProfile")
    // public ResponseEntity<PlayerProfile> addPlayerProfile(@PathVariable Long
    // userId, @RequestBody PlayerProfile profile) {
    // PlayerProfile createdProfile = userService.addPlayerProfile(userId, profile);
    // return createdProfile != null ? ResponseEntity.ok(createdProfile) :
    // ResponseEntity.notFound().build();
    // }

    // pls check this
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody LoginDetails newUserDTO) {
        try {
            User newUser = userService.addUser(newUserDTO); // call the addUser method in the service
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
