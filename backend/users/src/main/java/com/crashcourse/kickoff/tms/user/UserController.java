package com.crashcourse.kickoff.tms.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crashcourse.kickoff.tms.security.JwtUtil;
import com.crashcourse.kickoff.tms.user.dto.LoginDetails;
import com.crashcourse.kickoff.tms.user.dto.LoginResponseDTO;
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
    @PostMapping
    public ResponseEntity<User> signup(@RequestBody NewUserDTO newUserDTO) {
        try {
            User newUser = userService.addUser(newUserDTO);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{idToDelete}")
    public ResponseEntity<String> delete(@PathVariable Long idToDelete,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid" + token);
        }
        token = token.substring(7);
        // Extract the userId from the token using JwtUtil
        Long userIdFromToken = jwtUtil.extractUserId(token);
        if (!idToDelete.equals(userIdFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this profile");
        }

        System.out.println("id received: " + userIdFromToken);
        if (userService.getUserById(idToDelete) != null) {
            userService.deleteUserById(idToDelete);
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDetails loginDetails) throws Exception {
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

        // Assuming User has a getId() method to retrieve userId
        Long userId = user.getId();

        // Return both userId and jwtToken in the response
        LoginResponseDTO loginResponse = new LoginResponseDTO(userId, jwt);
        return ResponseEntity.ok(loginResponse);
    }
}
