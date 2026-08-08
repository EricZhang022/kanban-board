package com.kanbanboard.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanbanboard.backend.repo.UserRepository;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.dto.UserDTO;
import com.kanbanboard.backend.dto.Response;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
