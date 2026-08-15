package com.kanbanboard.backend.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID boardId; 

    private String boardName;

    @ManyToOne
    private User owner;

    @ManyToMany
    private List<User> collaborators;

    public Board() {}

    public Board(String boardName, User owner, List<User> collaborators) {
        this.boardName = boardName;
        this.owner = owner;
        this.collaborators = collaborators;
    }

    // for later 
    // @ManyToMany
    // private List<Columns> column

    public UUID getBoardId() {
        return boardId;
    }
    
    public String getBoardName() {
        return boardName;
    } 
    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getCollaborators() {
        return collaborators;
    }
    public void setCollaborators(List<User> collaborators) {
        this.collaborators = collaborators;
    } 
}
