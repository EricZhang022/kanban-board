package com.kanbanboard.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanbanboard.backend.service.ActivityLogService;

@RestController
@RequestMapping("/api/activity")
public class ActivityLogController {

    private final ActivityLogService activityLogService;
    public ActivityLogController(ActivityLogService activityLogService){
        this.activityLogService = activityLogService;
    }
    
}
