package com.kanbanboard.backend.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.User;

//custom, can add other search methods if needed later on
public interface BoardRepository extends JpaRepository<Board, UUID> {
    List<Board> findByOwner(User owner);
    List<Board> findByCollaboratorsContaining(User user);
}
