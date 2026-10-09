package com.kanbanboard.backend.dto;

import java.util.UUID;

import com.kanbanboard.backend.entity.BoardInvitation;
import com.kanbanboard.backend.enums.InvitationStatus;

public class BoardInvitationDTO {
    private UUID invitationId;
    private UserDTO sender;
    private UserDTO recipient;
    private InvitationStatus status;

    public BoardInvitationDTO(BoardInvitation boardInvitation) {
        this.invitationId = boardInvitation.getInvitationId();
        this.sender = new UserDTO(boardInvitation.getSender());
        this.recipient = new UserDTO(boardInvitation.getRecipient());
        this.status = boardInvitation.getStatus();
    }

    public UUID getInvitationId() {
        return invitationId;
    }

    public UserDTO getSender() {
        return sender;
    }

    public UserDTO getRecipient() {
        return recipient;
    }

    public InvitationStatus getStatus() {
        return status;
    }
}
