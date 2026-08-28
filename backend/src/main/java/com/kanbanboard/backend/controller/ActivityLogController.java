package com.kanbanboard.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanbanboard.backend.dto.ActivityLogDTO;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.service.ActivityLogService;

@RestController
@RequestMapping("/api/activitylog")
public class ActivityLogController {

    private final ActivityLogService activityLogService;
    public ActivityLogController(ActivityLogService activityLogService){
        this.activityLogService = activityLogService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<List<ActivityLogDTO>>> fetchActivityLogs(@PathVariable("id") UUID boardID){
        Response<List<ActivityLogDTO>> res = activityLogService.getLogs(boardID);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }
    
}
