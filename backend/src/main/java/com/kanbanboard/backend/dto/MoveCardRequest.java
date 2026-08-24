package com.kanbanboard.backend.dto;

import java.util.UUID;

public class MoveCardRequest {
    private UUID targetColumnId;
    private int newPosition;

    public UUID getTargetColumnId() { return targetColumnId; }
    public void setTargetColumnId(UUID targetColumnId) { this.targetColumnId = targetColumnId; }

    public int getNewPosition() { return newPosition; }
    public void setNewPosition(int newPosition) { this.newPosition = newPosition; }
}