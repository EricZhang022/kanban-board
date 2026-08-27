package com.kanbanboard.backend.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.kanbanboard.backend.entity.Notification;
import com.kanbanboard.backend.entity.User;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);
    List<Notification> findByRecipientAndReadTrueOrderByCreatedAtDesc(User recipient);
    long countByRecipientAndReadFalse(User recipient);
    void deleteByRecipientAndReadTrue(User recipient);

    @Modifying
    @Query("""
        UPDATE Notification n
        SET n.read = true
        WHERE n.recipient = :recipient
        AND n.read = false
    """)
    void markAllAsRead(User recipient);
    Optional<Notification> findByNotificationIdAndRecipient(UUID notificationId, User recipient);
    Optional<Notification> findByNotificationIdAndRecipientAndReadTrue(UUID notificationId, User recipient);
}
