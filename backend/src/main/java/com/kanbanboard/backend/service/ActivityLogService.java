package com.kanbanboard.backend.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

import com.kanbanboard.activitydetails.CreateCardLog;
import com.kanbanboard.backend.dto.ActivityLogDTO;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.entity.ActivityLog;
import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.entity.ActivityLog.ActionType;
import com.kanbanboard.backend.repo.ActivityLogRepository;
import com.kanbanboard.backend.repo.BoardColumnRepository;
import com.kanbanboard.backend.repo.BoardRepository;
import com.kanbanboard.backend.repo.CardRepository;

import tools.jackson.databind.ObjectMapper;

@Service
public class ActivityLogService {
    public interface LogTypes {
    };

    private final ActivityLogRepository activityLogRepository;
    private final ObjectMapper objectMapper;
    private final BoardRepository boardRepo;
    private final CardRepository cardRepo;
    private final BoardColumnRepository columnRepo;
    

    public ActivityLogService(ActivityLogRepository activityLogRepository, ObjectMapper objectMapper, BoardRepository boardRepo, CardRepository cardRepo, BoardColumnRepository columnRepo) {
        this.activityLogRepository = activityLogRepository;
        this.objectMapper = objectMapper;
        this.boardRepo = boardRepo;
        this.cardRepo = cardRepo;
        this.columnRepo = columnRepo;
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
    public Response<List<ActivityLogDTO>> getLogs(UUID boardID) {
        List<ActivityLogDTO> allAct = new ArrayList<>();
        Response<List<ActivityLogDTO>> res;
    
        System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        List<ActivityLog> getAllLogs = activityLogRepository.findByBoard_BoardIdOrderByCreatedAtDesc(boardID);
        System.out.println(getAllLogs);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        for (ActivityLog a : getAllLogs){
            String summary = "";
            if (a.getActionType() == (ActionType.create_card)){ //gonna implement this with switch later
                CreateCardLog obj = objectMapper.readValue(a.getDetails(), CreateCardLog.class);//turning jsonb string back into an obj
                String cardName = cardRepo.getReferenceById(obj.getCardID()).getTitle();
                String columnName = columnRepo.getReferenceById(obj.getColumnID()).getName();
                summary = "Created a card named " + cardName + " in " + columnName;
            }
            ActivityLogDTO temp = new ActivityLogDTO(a.getLogID(), a.getEditor().getUsername(), a.getActionType(), summary, a.getCreatedAt());
            allAct.add(temp);
        }
        System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");

        res = new Response<>(200, "Successfully got Activity Logs", allAct);
        return res;
        

    }
}
