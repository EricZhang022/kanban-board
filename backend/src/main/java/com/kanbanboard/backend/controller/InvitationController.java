package com.kanbanboard.backend.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.service.InvitationService;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {
    private final InvitationService invitationService;
    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    // When a user accepts an invitation for a board
    @PostMapping("/accept/{invitationId}")
    public ResponseEntity<Response<String>> acceptInvitation(Authentication auth, @PathVariable UUID invitationId) {
        UUID userId = UUID.fromString(auth.getName());
        Response<String> res = invitationService.acceptInvitation(userId, invitationId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
        
    }

    // When a user declines an invitation for a board
    @PostMapping("/decline/{invitationId}")
    public ResponseEntity<Response<String>> declineInvitation(Authentication auth, @PathVariable UUID invitationId) {
        UUID userId = UUID.fromString(auth.getName());
        Response<String> res = invitationService.declineInvitation(userId, invitationId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }
}
