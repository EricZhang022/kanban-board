package com.kanbanboard.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    
    @GetMapping("/")
    public String hello() {
        return "Backend is running! Spring Boot is successfully installed!";
    }

    // // testing purpose
    // @GetMapping("/secure")
    // public String secure() {
    //     return "You are authenticated";
    // }
}
