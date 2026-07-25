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
        Response<String> res;
        String username = request.getUsername();
        String password = request.getPassword();
        String confirmPass = request.getConfirmPassword();
        /*
        Things to check in future when db is setup:
        1) is username already taken?
        */

        // 1. Pass = ConfirmPass check
        if (!auth.passwordsMatch(password, confirmPass)) {
            res = new Response<>(400, "Confirm Password does not match your password");
            return ResponseEntity.badRequest().body(res);
        }
        if (!auth.isPassGood(password)) {
            res = new Response<>(400, "Password needs to be at least 8 characters, including at least a lower case, an upper case, a digit, and a special chaarcter.");
            return ResponseEntity.badRequest().body(res);
        }

        return ResponseEntity.ok(new Response<>(200, "OK"));

    }
    
}
