package com.kanbanboard.backend.dto;

import java.util.UUID;

import com.kanbanboard.backend.entity.User;

public class UserDTO {
    private UUID userid;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String profileColor;

    public UserDTO(User user) {
        this.userid = user.getUserid();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileColor = user.getProfileColor();
    }

    public UUID getUserId() {
        return userid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileColor() {
        return profileColor;
    }
}
