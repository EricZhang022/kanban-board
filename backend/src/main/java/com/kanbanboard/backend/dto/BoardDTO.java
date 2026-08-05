package com.kanbanboard.backend.dto;

import java.util.ArrayList;
import java.util.List;

import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.User;

public class BoardDTO {
    private String boardName;
    private String owner;
    private List<String> collaborators;

    public BoardDTO (Board board) {
        this.boardName = board.getBoardName();
        this.owner = board.getOwner().getUsername();
        this.collaborators = new ArrayList<>();
        for (User user : board.getCollaborators()) {
            this.collaborators.add(user.getUsername());
        }
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
    
}
