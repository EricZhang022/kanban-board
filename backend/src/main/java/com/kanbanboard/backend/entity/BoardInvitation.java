package com.kanbanboard.backend.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kanbanboard.backend.enums.InvitationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BoardInvitation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID invitationId;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    public BoardInvitation() {}

    public BoardInvitation(Board board, User sender, User recipient) {
        this.board = board;
        this.sender = sender;
        this.recipient = recipient;
        this.status = InvitationStatus.PENDING;

        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.expiresAt = now.plusDays(1);
    }

    public UUID getInvitationId() {
        return invitationId;
    }

    // board
    public Board getBoard() {
        return board;
    }
    public void setBoard(Board board) {
        this.board = board;
    }

    // sender
    public User getSender() {
        return sender;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }

    // recipient
    public User getRecipient() {
        return recipient;
    }
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    // status
    public InvitationStatus getStatus() {
        return status;
    }
    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    // createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // expiresAt
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
