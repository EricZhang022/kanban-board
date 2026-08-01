package com.kanbanboard.backend.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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


    // REMEMBER TO HASH PASS LATER CHECK BEFORE INSERT DB
    @PostMapping("/signup")
    public ResponseEntity<Response<String>> signup(@RequestBody SignUpRequest request) {
        Response<String> res = auth.signup(request);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }
    
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

}
