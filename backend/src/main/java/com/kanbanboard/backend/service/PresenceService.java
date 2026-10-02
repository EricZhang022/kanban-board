package com.kanbanboard.backend.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.kanbanboard.backend.dto.PresenceDTO;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.repo.UserRepository;

@Service 
public class PresenceService {
    
    private final Map<UUID, Map<UUID, Set<String>>> boardPresence = new ConcurrentHashMap<>();
    private final Map<String, PresenceSession> sessions = new ConcurrentHashMap<>();

    private final UserRepository userRepository;

    public PresenceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void userJoined(String sessionId, UUID boardId, UUID userId) {
        boardPresence.computeIfAbsent(boardId, id -> new ConcurrentHashMap<>())
            .computeIfAbsent(userId, id -> ConcurrentHashMap.newKeySet()).add(sessionId);
        sessions.put(sessionId, new PresenceSession(boardId, userId));
    }

    public UUID userLeft(String sessionId) {
        PresenceSession session = sessions.remove(sessionId);

        if (session == null) {
            return null;
        }

        UUID boardId = session.getBoardId();
        UUID userId = session.getUserId();

        Map<UUID, Set<String>> users = boardPresence.get(boardId);

        if (users != null) {
            Set<String> userSessions = users.get(userId);

            if (userSessions != null) {
                userSessions.remove(sessionId);

                // User still has another tab/session open
                if (userSessions.isEmpty()) {
                    users.remove(userId);
                }
            }

            // No users left on this board
            if (users.isEmpty()) {
                boardPresence.remove(boardId);
            }
        }

        return boardId;
    }

    public Set<UUID> getUsers(UUID boardId) {
        Map<UUID, Set<String>> users = boardPresence.get(boardId);

        if (users == null) {
            return Set.of();
        }

        return users.keySet();
    }

    public List<PresenceDTO> getPresence(UUID boardId) {
        return getUsers(boardId).stream()
            .map(userId -> {
                User user = userRepository.findById(userId)
                        .orElseThrow();

                return new PresenceDTO(
                        user.getUserid(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getProfileColor()
                );
            })
            .toList();
    }
}
