package com.kanbanboard.backend.service;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import org.springframework.messaging.simp.SimpMessagingTemplate;

@Component
public class PresenceEventListener {

    private final PresenceService presenceService;
    private final SimpMessagingTemplate messagingTemplate;

    public PresenceEventListener(PresenceService presenceService, SimpMessagingTemplate messagingTemplate) {
        this.presenceService = presenceService;
        this.messagingTemplate = messagingTemplate;
    }

    // This gets called when a user closes the browser tab
    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = accessor.getSessionId();
        UUID boardId = presenceService.userLeft(sessionId);

        if (boardId == null) {
            return;
        }

        messagingTemplate.convertAndSend("/topic/boards/" + boardId + "/presence", presenceService.getPresence(boardId));
    }
}