package com.kanbanboard.backend.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.BoardInvitation;
import com.kanbanboard.backend.entity.Notification;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.enums.NotificationType;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByRecipient(User recipienrt);
    List<Notification> findByRecipientOrderByReadAscCreatedAtDesc(User recipient);
    List<Notification> findByRecipientAndReadTrueOrderByCreatedAtDesc(User recipient);
    long countByRecipientAndReadFalse(User recipient);
    void deleteByRecipientAndReadTrue(User recipient);
    Optional<Notification> findByNotificationIdAndRecipient(UUID notificationId, User recipient);
    Optional<Notification> findByNotificationIdAndRecipientAndReadTrue(UUID notificationId, User recipient);
    Optional<Notification> findByInvitationAndType(BoardInvitation invitation, NotificationType type);

    @Modifying
    @Query("""
        DELETE FROM Notification n
        WHERE n.board = :board
    """)
    void deleteByBoard(@Param("board") Board board);

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
