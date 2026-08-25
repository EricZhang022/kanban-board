package com.kanbanboard.backend.service;

import java.time.Instant;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

import com.kanbanboard.backend.entity.ActivityLog;
import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.entity.ActivityLog.ActionType;
import com.kanbanboard.backend.repo.ActivityLogRepository;

import tools.jackson.databind.ObjectMapper;

@Service
public class ActivityLogService {
    public interface LogTypes {
    };

    private final ActivityLogRepository activityLogRepository;
    private final ObjectMapper objectMapper;

    public ActivityLogService(ActivityLogRepository activityLogRepository, ObjectMapper objectMapper) {
        this.activityLogRepository = activityLogRepository;
        this.objectMapper = objectMapper;
    }

    // insert into db
    public void logActivity(Board board, User editor, ActionType actionType, Object details){
            ActivityLog activityLog = new ActivityLog();
            activityLog.setBoard(board);
            activityLog.setEditor(editor);
            activityLog.setActionType(actionType);
            activityLog.setDetails(objectMapper.writeValueAsString(details));
            activityLog.setCreatedAt(Instant.now());
            activityLogRepository.save(activityLog);

    }

    // get the logs for the current board
    public List<String> getLogs(Board board) {
        return null;
    }
}
