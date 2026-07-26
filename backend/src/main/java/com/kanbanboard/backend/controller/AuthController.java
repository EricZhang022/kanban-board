package com.kanbanboard.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.dto.SignUpRequest;

import com.kanbanboard.backend.service.Auth;

@RestController
public class AuthController {

    // -> auth need exist by time signup runs
    private final Auth auth;
    public AuthController(Auth auth) {
        this.auth = auth;
    }


    // REMEMBER TO HASH PASS LATER CHECK BEFORE INSERT DB
    @PostMapping("/api/signup")
    public ResponseEntity<Response<String>> signup(@RequestBody SignUpRequest request) {
        Response<String> res = auth.signup(request);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }
    
}
