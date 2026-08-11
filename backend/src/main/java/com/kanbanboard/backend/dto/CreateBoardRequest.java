package com.kanbanboard.backend.dto;

import java.util.List;

public class CreateBoardRequest {
    private String boardName;
    private List<String> collaborators;

    public String getBoardName() {
        return boardName;
    }
    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
    
    public List<String> getCollaborators() {
        return collaborators;
    }
    public void setCollaborators(List<String> collaborators) {
        this.collaborators = collaborators;
    }


}
