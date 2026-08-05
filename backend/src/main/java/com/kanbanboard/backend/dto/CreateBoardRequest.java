package com.kanbanboard.backend.dto;

import java.util.List;

public class CreateBoardRequest {
    private String boardName;
    private String owner;
    private List<String> collaborators;

    public String getBoardName() {
        return boardName;
    }
    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getOwner() {
        return owner;
    }
    
    public List<String> getCollaborators() {
        return collaborators;
    }


}
