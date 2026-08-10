package com.kanbanboard.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.kanbanboard.backend.repo.UserRepository;
import com.kanbanboard.backend.service.Auth;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.dto.UserDTO;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.dto.FirstNameRequest;
import com.kanbanboard.backend.dto.LastNameRequest;
import com.kanbanboard.backend.dto.UsernameRequest;
import com.kanbanboard.backend.dto.EmailRequest;
import com.kanbanboard.backend.dto.ChangePasswordRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final Auth auth;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserController(UserRepository userRepository, Auth auth, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.auth = auth;
        this.encoder = encoder;
    }

    // Triggers when the user tries to access their dashboard or anything related to their profile
    @GetMapping("/me")
    public ResponseEntity<Response<UserDTO>> getCurrentUser(Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        UserDTO userDTO = new UserDTO(user);

        Response<UserDTO> res = new Response<>(200, "OK", userDTO);

        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    // Triggers when the user tries to update their first name
    @PutMapping("/me/first-name")
    public ResponseEntity<Response<String>> updateFirstName(@RequestBody FirstNameRequest request, Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String firstName = request.getFirstName();

        if (!auth.isFirstNameValid(firstName)) {
            return ResponseEntity.status(400)
                .body(new Response<>(400, "First name can only contain letters"));
        }

        user.setFirstName(firstName);

        userRepository.save(user);

        return ResponseEntity.ok(
            new Response<>(200, "First name updated successfully")
        );
    }

    // Triggers when the user tries to update their last name
    @PutMapping("/me/last-name")
    public ResponseEntity<Response<String>> updateLastName(@RequestBody LastNameRequest request, Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String lastName = request.getLastName();

        if (!auth.isLastNameValid(lastName)) {
            return ResponseEntity.status(400)
                .body(new Response<>(400, "Last name can only contain letters"));
        }

        user.setLastName(lastName);

        userRepository.save(user);

        return ResponseEntity.ok(
            new Response<>(200, "Last name updated successfully")
        );
    }

    // Triggers when the user tries to update their username
    @PutMapping("/me/username")
    public ResponseEntity<Response<String>> updateUsername(@RequestBody UsernameRequest request, Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String newUsername = request.getUsername();

        if (!auth.isUsernameValid(newUsername)) {
            return ResponseEntity.status(400)
                .body(new Response<>(400, "Username may only contain alphanumeric characters"));
        }

        if (auth.isUsernameExist(newUsername)) {
            return ResponseEntity.status(400)
                .body(new Response<>(400, "Username is already taken"));
        }

        user.setUsername(newUsername);

        userRepository.save(user);

        return ResponseEntity.ok(
            new Response<>(200, "Username updated successfully")
        );
    }

    // Triggers when the user tries to update their email
    @PutMapping("/me/email")
    public ResponseEntity<Response<String>> updateEmail(@RequestBody EmailRequest request, Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String email = request.getEmail();

        if (!auth.isEmailValid(email)) {
            return ResponseEntity.status(400)
                .body(new Response<>(400, "Invalid Email format"));
        }

        if (auth.isEmailExist(email)) {
            return ResponseEntity.status(400)
                .body(new Response<>(400, "Email is already in use"));
        }

        user.setEmail(email);

        userRepository.save(user);

        return ResponseEntity.ok(
            new Response<>(200, "Email updated successfully")
        );
    }

    // Triggers when the user tries to change their password
    @PutMapping("/me/password")
    public ResponseEntity<Response<String>> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify current password against the hashed one stored in the database
        if (!encoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(401)
                .body(new Response<>(401, "Current password is incorrect"));
        }

        // Verify that the current and the new passwords aren't the same
        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            return ResponseEntity.status(400)
                .body(new Response<>(400, "New password cannot match your current password"));
        }

        // Hash the new password
        String hashedPassword = encoder.encode(request.getNewPassword());

        user.setPassword(hashedPassword);

        userRepository.save(user);

        return ResponseEntity.ok(
            new Response<>(200, "Password changed successfully")
        );
    }
}
