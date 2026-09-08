package com.kanbanboard.backend.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.BoardInvitation;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.entity.Column;

public class BoardDTO {
    private UUID boardId;
    private String boardName;
    private String owner;
    private String role;
    private List<UserDTO> collaborators;
    private List<BoardInvitationDTO> pendingInvitations;
    private List<ColumnDTO> columns;

    public BoardDTO (Board board, String currUser) {
        this.boardId = board.getBoardId();
        this.boardName = board.getBoardName();
        this.owner = board.getOwner().getUsername();
        this.collaborators = new ArrayList<>();
        for (User user : board.getCollaborators()) {
            this.collaborators.add(new UserDTO(user));
        }
        this.role = board.getOwner().getUsername().equals(currUser) ? "owner" : "collaborator";
        this.columns = new ArrayList<>();
        if (board.getColumns() != null) {
            for (Column col : board.getColumns()) {
            this.columns.add(new ColumnDTO(col));
            }
        }
    }

    public BoardDTO (Board board, String currUser, List<BoardInvitation> pendingInvitations) {
        this.boardId = board.getBoardId();
        this.boardName = board.getBoardName();
        this.owner = board.getOwner().getUsername();
        this.collaborators = new ArrayList<>();
        for (User user : board.getCollaborators()) {
            this.collaborators.add(new UserDTO(user));
        }
        this.pendingInvitations = new ArrayList<>();
        for (BoardInvitation invitation: pendingInvitations) {
            this.pendingInvitations.add(new BoardInvitationDTO(invitation));
        }
        this.role = board.getOwner().getUsername().equals(currUser) ? "owner" : "collaborator";
        this.columns = new ArrayList<>();
        if (board.getColumns() != null) {
            for (Column col : board.getColumns()) {
            this.columns.add(new ColumnDTO(col));
            }
        }
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
    public List<UserDTO> getCollaborators() {
        return collaborators;
    }
    public List<BoardInvitationDTO> getPendingInvitations() {
        return pendingInvitations;
    }
    public String getRole() {
        return role;
    }
    public List<ColumnDTO> getColumns() {
        return columns;
    }
    
}
