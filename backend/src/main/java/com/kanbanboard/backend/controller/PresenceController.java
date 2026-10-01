package com.kanbanboard.backend.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.kanbanboard.backend.dto.PresenceDTO;
import com.kanbanboard.backend.service.PresenceService;

@Controller 
public class PresenceController {
    
    private final PresenceService presenceService;
    private final SimpMessagingTemplate messagingTemplate;

    public PresenceController(PresenceService presenceService, SimpMessagingTemplate messagingTemplate) {
        this.presenceService = presenceService;
        this.messagingTemplate = messagingTemplate;
    }

    // This gets called when the users clicks on a board
    @MessageMapping("/boards/{boardId}/join")
    public void joinBoard(@DestinationVariable UUID boardId, Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        UUID userId = UUID.fromString(principal.getName());
        String sessionId = headerAccessor.getSessionId();

        presenceService.userJoined(sessionId, boardId, userId);

        broadcastPresence(boardId);
    }

    // This gets called when the user clicks away from the board
    @MessageMapping("/boards/{boardId}/leave")
    public void leaveBoard(@DestinationVariable UUID boardId, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();

        UUID removedBoardId = presenceService.userLeft(sessionId);

        if (removedBoardId != null) {
            broadcastPresence(boardId);
        }
    }

    // Sends the list of users on a board back to the frontend
    private void broadcastPresence(UUID boardId) {
        
        List<PresenceDTO> users = presenceService.getPresence(boardId);

        messagingTemplate.convertAndSend("/topic/boards/" + boardId + "/presence", users);
    }
}
