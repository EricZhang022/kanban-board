package com.kanbanboard.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kanbanboard.backend.dto.NotificationDTO;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.BoardInvitation;
import com.kanbanboard.backend.entity.Notification;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.enums.NotificationType;
import com.kanbanboard.backend.repo.NotificationRepository;
import com.kanbanboard.backend.repo.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class NotificationService {

    private final UserRepository userRepo;
    private final NotificationRepository notifRepo;

    public NotificationService(UserRepository userRepo, NotificationRepository notifRepo) {
        this.userRepo = userRepo;
        this.notifRepo = notifRepo;
    }

    public Response<List<NotificationDTO>> fetchAllNotifications(UUID userId) {
        User recipient = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("Recipient not found on getting notifications"));

        List<Notification> notifs = notifRepo.findByRecipientOrderByReadAscCreatedAtDesc(recipient);
        List<NotificationDTO> allNotifDTO = new ArrayList<>();

        for (Notification notif : notifs) {
            allNotifDTO.add(new NotificationDTO(notif));
        }

        return new Response<>(200, "All notifications successfully retrieved", allNotifDTO);
    }

    public Response<NotificationDTO> createNotification(UUID userId, NotificationType type) {
        User recipient = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("Recipient not found on creating notification"));

        Notification notif = new Notification(recipient, type); 

        notifRepo.save(notif);
        NotificationDTO notifDTO = new NotificationDTO(notif);

        return new Response<>(200, "Notification is successfully created", notifDTO);
        
    }

    @Transactional
    public Response<NotificationDTO> sendNotification(UUID senderId, UUID recipientId, NotificationType type, Board board, BoardInvitation boardInvitation) {
        User sender = userRepo.findById(senderId)
            .orElseThrow(() -> new RuntimeException("Sender not found on sending notification"));

        User recipient = userRepo.findById(recipientId)
            .orElseThrow(() -> new RuntimeException("Recipient not found on getting notification"));

        Notification notif = new Notification(recipient, type);
        notif.setSender(sender);
        notif.setBoard(board);
        notif.setInvitation(boardInvitation);

        notifRepo.save(notif);
        NotificationDTO notifDTO = new NotificationDTO(notif);

        return new Response<>(200, "Notification is successfully sent", notifDTO);
    }

    @Transactional
    public Response<String> markAllNotificationsAsRead(UUID userId) {
        User recipient = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException(
                "Recipient not found on marking notifications as read"
            ));

        notifRepo.markAllAsRead(recipient, NotificationType.BOARD_INVITATION);

        return new Response<>(200, "All notifications successfully marked as read");
    }

    @Transactional
    public Response<String> markNotificationAsRead(UUID userId, UUID notificationId) {
        User recipient = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException(
                "Recipient not found on marking notification as read"
            ));

        Notification notif = notifRepo
            .findByNotificationIdAndRecipient(notificationId, recipient)
            .orElseThrow(() -> new RuntimeException(
                "Notification not found"
            ));

        notif.setRead(true);

        notifRepo.save(notif);

        return new Response<>(200, "Notification successfully marked as read");
    }

    @Transactional
    public Response<String> deleteAllReadNotifications(UUID userId) {
        User recipient = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("Recipient not found on getting notification"));

        notifRepo.deleteByRecipientAndReadTrue(recipient);

        return new Response<>(200, "Read notifications are successfully deleted");
    }

    @Transactional
    public Response<String> deleteReadNotification(UUID userId, UUID notificationId) {
        User recipient = userRepo.findById(userId)
        .orElseThrow(() -> new RuntimeException(
            "Recipient not found on deleting notification"
        ));

        Notification notification = notifRepo
            .findByNotificationIdAndRecipientAndReadTrue(
                notificationId,
                recipient
            )
            .orElseThrow(() -> new RuntimeException(
                "Read notification not found"
            ));

        notifRepo.delete(notification);

        return new Response<>(200, "Notification successfully deleted");
    }
}
