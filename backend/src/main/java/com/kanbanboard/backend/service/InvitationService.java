package com.kanbanboard.backend.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.BoardInvitation;
import com.kanbanboard.backend.entity.Notification;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.enums.InvitationStatus;
import com.kanbanboard.backend.enums.NotificationType;
import com.kanbanboard.backend.repo.BoardRepository;
import com.kanbanboard.backend.repo.InvitationRepository;
import com.kanbanboard.backend.repo.NotificationRepository;

import jakarta.transaction.Transactional;

@Service
public class InvitationService {
    private final NotificationService notificationService;
    private final BoardRepository boardRepo;
    private final InvitationRepository invitationRepo;
    private final NotificationRepository notifRepo;

    public InvitationService(NotificationService notificationService, BoardRepository boardRepo, InvitationRepository invitationRepo, NotificationRepository notifRepo) {
        this.notificationService = notificationService;
        this.boardRepo = boardRepo;
        this.invitationRepo = invitationRepo;
        this.notifRepo = notifRepo;
    }

    @Transactional
    public Response<String> acceptInvitation(UUID userId, UUID invitationId) {

        BoardInvitation invitation = invitationRepo.findById(invitationId)
            .orElseThrow(() -> new RuntimeException("Invitation not found"));

        // Verify the logged-in user owns this invitation
        if (!invitation.getRecipient().getUserid().equals(userId)) {
            return new Response<>(403, "You are not authorized to accept this invitation");
        }

        // Prevent accepting twice
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            return new Response<>(400, "This invitation is no longer pending");
        }

        Board board = invitation.getBoard();
        User recipient = invitation.getRecipient();

        // Add user as collaborator
        board.getCollaborators().add(recipient);

        // Update invitation status
        invitation.setStatus(InvitationStatus.ACCEPTED);

        Notification notification = notifRepo.findByInvitation(invitation)
            .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);

        boardRepo.save(board);
        invitationRepo.save(invitation);
        notifRepo.save(notification);

        User owner = board.getOwner();

        notificationService.sendNotification(userId, owner.getUserid(), NotificationType.BOARD_INVITATION_ACCEPTED, board, invitation);

        return new Response<>(200, "Invitation accepted");
    }

    @Transactional
    public Response<String> declineInvitation(UUID userId, UUID invitationId) {
        BoardInvitation invitation = invitationRepo.findById(invitationId)
            .orElseThrow(() -> new RuntimeException("Invitation not found"));

        // Verify recipient
        if (!invitation.getRecipient().getUserid().equals(userId)) {
            return new Response<>(
                403,
                "You are not authorized to decline this invitation"
            );
        }

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            return new Response<>(
                400,
                "This invitation is no longer pending"
            );
        }

        // Update invitation status
        invitation.setStatus(InvitationStatus.DECLINED);

        Notification notification = notifRepo.findByInvitation(invitation)
            .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);

        invitationRepo.save(invitation);
        notifRepo.save(notification);

        Board board = invitation.getBoard();
        User owner = board.getOwner();

        notificationService.sendNotification(userId, owner.getUserid(), NotificationType.BOARD_INVITATION_DECLINED, board, invitation);

        return new Response<>(200, "Invitation declined");
    }

}
