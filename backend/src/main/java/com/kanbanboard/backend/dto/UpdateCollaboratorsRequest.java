package com.kanbanboard.backend.dto;

import java.util.List;

public class UpdateCollaboratorsRequest {
    private List<String> collaborators;

    public List<String> getCollaborators() {
        return collaborators;
    }
    public void setCollaborators(List<String> collaborators) {
        this.collaborators = collaborators;
    }
}
