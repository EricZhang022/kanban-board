package com.kanbanboard.backend.dto;

import java.time.Instant;
import java.util.UUID;

import com.kanbanboard.backend.entity.ActivityLog.ActionType;

public class ActivityLogDTO {
    private UUID logID;
    private String editorName;
    private ActionType actionType;
    private String summary;
    private Instant createdAt;

    public ActivityLogDTO(UUID logID, String editorName, ActionType actionType, String summary, Instant createdAt) {
        this.logID = logID;
        this.editorName = editorName;
        this.actionType = actionType;
        this.summary = summary;
        this.createdAt = createdAt;
    }




    public UUID getLogID() {
        return logID;
    }
    public void setLogID(UUID logID) {
        this.logID = logID;
    }
    public String getEditorName() {
        return editorName;
    }
    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }
    public ActionType getActionType() {
        return actionType;
    }
    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    
}