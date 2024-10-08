package com.crashcourse.kickoff.tms.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crashcourse.kickoff.tms.security.JwtUtil;
import com.crashcourse.kickoff.tms.user.dto.LoginDetails;
import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.model.User;
import com.crashcourse.kickoff.tms.user.service.UserService;

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
    @PostMapping("")
    public ResponseEntity<User> signup(@RequestBody NewUserDTO newUserDTO) {
        try {
            User newUser = userService.addUser(newUserDTO); 
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
        final User user = userService.loadUserByUsername(loginDetails.getUsername());
        final String jwt = jwtUtil.generateToken(user);
        return ResponseEntity.ok(jwt);
    }
}
