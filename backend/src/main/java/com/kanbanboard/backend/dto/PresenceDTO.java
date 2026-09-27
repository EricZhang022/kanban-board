package com.kanbanboard.backend.dto;

import java.util.UUID;

public class PresenceDTO {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String username;

    public PresenceDTO(UUID userId, String firstName, String lastName, String username) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public UUID getUserId() {
        return userId;
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
}