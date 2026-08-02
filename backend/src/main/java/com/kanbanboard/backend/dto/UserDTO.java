package com.kanbanboard.backend.dto;

import com.kanbanboard.backend.entity.User;

public class UserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;

    public UserDTO(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.email = user.getEmail();
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
}
