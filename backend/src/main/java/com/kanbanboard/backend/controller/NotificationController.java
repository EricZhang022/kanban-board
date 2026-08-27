package com.kanbanboard.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.dto.NotificationDTO;
import com.kanbanboard.backend.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<Response<List<NotificationDTO>>> fetchNotifications(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<List<NotificationDTO>> res = notificationService.fetchAllNotifications(userId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @PatchMapping("/read")
    public ResponseEntity<Response<String>> markAllNotificationsAsRead(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<String> res = notificationService.markAllNotificationsAsRead(userId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @PatchMapping("/read/{id}")
    public ResponseEntity<Response<String>> markNotificationAsRead(Authentication auth, @PathVariable UUID notifId) {
        UUID userId = UUID.fromString(auth.getName());
        Response<String> res = notificationService.markNotificationAsRead(userId, notifId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @DeleteMapping("/read")
    public ResponseEntity<Response<String>> deleteAllReadNotifications(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<String> res = notificationService.deleteAllReadNotifications(userId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @DeleteMapping("/read/{id}")
    public ResponseEntity<Response<String>> deleteAReadNotification(Authentication auth, @PathVariable UUID notifId) {
        UUID userId = UUID.fromString(auth.getName());
        Response<String> res = notificationService.deleteReadNotification(userId, notifId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

}
