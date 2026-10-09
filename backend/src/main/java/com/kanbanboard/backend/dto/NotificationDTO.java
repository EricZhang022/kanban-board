package com.kanbanboard.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kanbanboard.backend.entity.Notification;
import com.kanbanboard.backend.enums.InvitationStatus;
import com.kanbanboard.backend.enums.NotificationType;

public class NotificationDTO {
    private UUID notificationId;
    private NotificationType type;
    private UserDTO sender;
    private BoardDTO board;
    private boolean read;
    private LocalDateTime createdAt;
    private UUID invitationId;
    private InvitationStatus invitationStatus;

    public NotificationDTO(Notification notification) {
        this.notificationId = notification.getNotificationId();
        this.type = notification.getType();

        // Notifications may not have a sender
        this.sender = notification.getSender() != null ? new UserDTO(notification.getSender()) : null;
        this.board = notification.getBoard() != null ? new BoardDTO(notification.getBoard(), notification.getRecipient().getUsername()) : null;
        this.read = notification.isRead();
        this.createdAt = notification.getCreatedAt();
        this.invitationId = notification.getInvitation() != null ? notification.getInvitation().getInvitationId() : null;
        this.invitationStatus = notification.getInvitation() != null ? notification.getInvitation().getStatus() : null;
    }

    public UUID getNotificationId() {
        return notificationId;
    }

    public NotificationType getType() {
        return type;
    }

    public UserDTO getSender() {
        return sender;
    }

    public BoardDTO getBoard() {
        return board;
    }

    public boolean isRead() {
        return read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UUID getInvitationId() {
        return invitationId;
    }

    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }
}
