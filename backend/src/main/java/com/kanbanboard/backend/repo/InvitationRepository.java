package com.kanbanboard.backend.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kanbanboard.backend.entity.BoardInvitation;

public interface InvitationRepository extends JpaRepository<BoardInvitation, UUID> {
}
