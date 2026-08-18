package com.kanbanboard.backend.repo;

import java.util.UUID;
import com.kanbanboard.backend.entity.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardColumnRepository extends JpaRepository<Column, UUID> {
}