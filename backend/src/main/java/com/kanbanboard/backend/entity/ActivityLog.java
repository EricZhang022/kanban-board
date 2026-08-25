package com.kanbanboard.backend.entity;
import java.time.Instant;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "activity_log")
public class ActivityLog {

    public enum ActionType{ //add future action types here
        create_card,
        move_card,
        delete_card,
        create_column,
        edit_card_description,
        assign_card,

    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID logId;

    @ManyToOne
    private Board board;

    @ManyToOne
    private User editor;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @JdbcTypeCode(SqlTypes.JSON)
    @jakarta.persistence.Column(columnDefinition = "jsonb")
    private String details; //stores different data depending on actiontype

    private Instant createdAt;

    public ActivityLog(){}

    public ActivityLog(User editor, ActionType actionType, String details, Instant createdAt){
        this.editor = editor;
        this.actionType = actionType;
        this.details = details;
        this.createdAt = createdAt;
    }


    public void setEditor(User editor){
        this.editor = editor;
    }
    public void setActionType(ActionType actionType){
        this.actionType = actionType;
    }
    public void setDetails(String details){
        this.details = details;
    }
    public void setCreatedAt(Instant createdAt){
        this.createdAt = createdAt;
    }
    public void setBoard(Board board){
        this.board = board;
    }

    public UUID getLogID(){
        return this.logId;
    }
    public User getEditor(){
        return this.editor;
    }
    public ActionType getActionType(){
        return this.actionType;
    }
    public String getDetails(){
        return this.details;
    }
    public Instant getCreatedAt(){
        return this.createdAt;
    }
}
