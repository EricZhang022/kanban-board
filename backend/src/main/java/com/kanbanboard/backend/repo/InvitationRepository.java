package com.kanbanboard.backend.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.BoardInvitation;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.enums.InvitationStatus;

public interface InvitationRepository extends JpaRepository<BoardInvitation, UUID> {
    boolean existsByBoardAndRecipientAndStatus(
        Board board,
        User recipient,
        InvitationStatus status
    );
}
