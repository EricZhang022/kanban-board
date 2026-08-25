package com.kanbanboard.backend.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kanbanboard.backend.entity.ActivityLog;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID> {
    // Optional<ActivityLog> getLogsByBoard(UUID boardID);
    
}
