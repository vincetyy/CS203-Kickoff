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
import com.crashcourse.kickoff.tms.security.JwtAuthService;
import com.crashcourse.kickoff.tms.user.dto.LoginDetails;
import com.crashcourse.kickoff.tms.user.dto.LoginResponseDTO;
import com.crashcourse.kickoff.tms.user.dto.NewUserDTO;
import com.crashcourse.kickoff.tms.user.dto.UserResponseDTO;
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
    private final JwtAuthService jwtAuthService;

    public UserController(UserService userService, JwtAuthService jwtAuthService) {
        this.userService = userService;
        this.jwtAuthService = jwtAuthService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable Long user_id,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String token) {
        // Validate token and authorization
        ResponseEntity<String> authResponse = jwtAuthService.validateToken(token, user_id);
        if (authResponse != null)
            return authResponse;

        try {
            User foundUser = userService.getUserById(user_id);
            if (foundUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User with ID " + user_id + " not found.");
            }

            return ResponseEntity.ok(foundUser);
        } catch (Exception e) {
            // Log the error for debugging purposes
            System.err.println("Error fetching user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }
    }

    @GetMapping("/publicinfo/{user_id}")
    public ResponseEntity<?> getUserPublicInfoById(
            @PathVariable Long user_id,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        try {
            User foundUser = userService.getUserById(user_id);
            if (foundUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User with ID " + user_id + " not found.");
            }

            UserResponseDTO userDTO = new UserResponseDTO(
                    foundUser.getId(),
                    foundUser.getUsername());

            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            // Log the error for debugging purposes
            System.err.println("Error fetching user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }
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

        // Validate token and authorization
        ResponseEntity<String> authResponse = jwtAuthService.validateToken(token, idToDelete);
        if (authResponse != null)
            return authResponse;

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
