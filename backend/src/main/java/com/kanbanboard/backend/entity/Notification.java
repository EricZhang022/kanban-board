package com.kanbanboard.backend.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.kanbanboard.backend.enums.NotificationType;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID notificationId;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private boolean read = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "invitation_id")
    private BoardInvitation invitation;

    public Notification() {}

    public Notification(User recipient, NotificationType type) {
        this.recipient = recipient;
        this.type = type;
    }

    // userId
    public UUID getNotificationId() {
        return notificationId;
    }

    // recipient
    public User getRecipient() {
        return recipient;
    }
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    // type
    public NotificationType getType() {
        return type;
    }
    public void setType(NotificationType type) {
        this.type = type;
    }

    // sender
    public User getSender() {
        return sender;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }

    // board
    public Board getBoard() {
        return board;
    }
    public void setBoard(Board board) {
        this.board = board;
    }

    // read
    public boolean isRead() {
        return read;
    }
    public void setRead(boolean read) {
        this.read = read;
    }

    // createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // invitation
    public BoardInvitation getInvitation() {
        return invitation;
    }
    public void setInvitation(BoardInvitation invitation) {
        this.invitation = invitation;
    }
}
