package com.kanbanboard.backend.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.User;

public class BoardDTO {
    private UUID boardId;
    private String boardName;
    private String owner;
    private String role;
    private List<String> collaborators;

    public BoardDTO (Board board, String currUser) {
        this.boardId = board.getBoardId();
        this.boardName = board.getBoardName();
        this.owner = board.getOwner().getUsername();
        this.collaborators = new ArrayList<>();
        for (User user : board.getCollaborators()) {
            this.collaborators.add(user.getUsername());
        }
        this.role = board.getOwner().getUsername().equals(currUser) ? "owner" : "collaborator";
    }

    public UUID getBoardId() {
        return boardId;
    }

    public String getBoardName() {
        return boardName;
    }
    public String getOwner() {
        return owner;
    }
    public List<String> getCollaborators() {
        return collaborators;
    }
    public String getRole() {
        return role;
    }
    
}
