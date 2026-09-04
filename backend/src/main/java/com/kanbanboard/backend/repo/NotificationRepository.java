package com.kanbanboard.backend.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.BoardInvitation;
import com.kanbanboard.backend.entity.Notification;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.enums.NotificationType;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByRecipientOrderByReadAscCreatedAtDesc(User recipient);
    List<Notification> findByRecipientAndReadTrueOrderByCreatedAtDesc(User recipient);
    long countByRecipientAndReadFalse(User recipient);
    void deleteByRecipientAndReadTrue(User recipient);

    @Modifying
    @Query("""
        UPDATE Notification n
        SET n.read = true
        WHERE n.recipient = :recipient
        AND n.read = false
        AND n.type <> :excludedType
    """)
    void markAllAsRead(User recipient, NotificationType excludedType);
    Optional<Notification> findByNotificationIdAndRecipient(UUID notificationId, User recipient);
    Optional<Notification> findByNotificationIdAndRecipientAndReadTrue(UUID notificationId, User recipient);
    Optional<Notification> findByInvitation(BoardInvitation invitation);

    @Modifying
    @Query("""
        DELETE FROM Notification n
        WHERE n.invitation IN (
            SELECT bi
            FROM BoardInvitation bi
            WHERE bi.board = :board
        )
    """)
    void deleteByBoardInvitations(Board board);
}
