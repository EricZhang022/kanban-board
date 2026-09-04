package com.kanbanboard.backend.repo;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.BoardInvitation;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.enums.InvitationStatus;

public interface InvitationRepository extends JpaRepository<BoardInvitation, UUID> {
    boolean existsByBoardAndRecipientAndStatusAndExpiresAtAfter(
        Board board,
        User recipient,
        InvitationStatus status,
        LocalDateTime time
    );

    @Modifying
    @Query("""
        DELETE FROM BoardInvitation bi
        WHERE bi.board = :board
    """)
    void deleteByBoard(Board board);
}
