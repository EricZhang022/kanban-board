package com.kanbanboard.backend.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.dto.SignUpRequest;
import com.kanbanboard.backend.dto.LoginRequest;

import com.kanbanboard.backend.service.Auth;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // -> auth need exist by time signup runs
    private final Auth auth;
    public AuthController(Auth auth) {
        this.auth = auth;
    }

    // Triggers when the user tries to access anything that requires authentication
    @GetMapping("/check")
    public ResponseEntity<Response<String>> checkAuthentication(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                .body(new Response<>(401, "Unauthorized", null));
        }

        return ResponseEntity.ok(
            new Response<>(200, "Authenticated", null)
        );
    }

    // Signup route (password is hashed within the signup function in Auth class)
    @PostMapping("/signup")
    public ResponseEntity<Response<String>> signup(@RequestBody SignUpRequest request) {
        Response<String> res = auth.signup(request);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }
    
    // Login route
    @PostMapping("/login")
    public ResponseEntity<Response<String>> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Response<String> res = auth.login(request);

        if (res.getStatusCode() == 200) {
            String token = res.getData();
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
            .httpOnly(true)
            .secure(false) // ***true in production -> requires HTTPS *** -> rn in localhost development stage so set false
            .sameSite("Lax")
            .path("/")
            .maxAge(60 * 60 * 24)
            .build();

            response.addHeader("Set-Cookie", cookie.toString());
            res.setData(null);
        }

        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    // When user logs out
    @PostMapping("/logout")
    public ResponseEntity<Response<String>> logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
            .httpOnly(true)
            .secure(false) // true in production
            .sameSite("Lax")
            .path("/")
            .maxAge(0) // deletes cookie
            .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(
            new Response<>(200, "Logged out", null)
        );
    }
}
