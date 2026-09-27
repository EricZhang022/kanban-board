package com.kanbanboard.backend.service;

import java.util.UUID;

public class PresenceSession {

    private final UUID boardId;
    private final UUID userId;

    public PresenceSession(UUID boardId, UUID userId) {
        this.boardId = boardId;
        this.userId = userId;
    }

    public UUID getBoardId() {
        return boardId;
    }

    public UUID getUserId() {
        return userId;
    }
}