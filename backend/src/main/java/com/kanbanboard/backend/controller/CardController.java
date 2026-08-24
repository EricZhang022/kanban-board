package com.kanbanboard.backend.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.kanbanboard.backend.dto.CardDTO;
import com.kanbanboard.backend.dto.CreateCardRequest;
import com.kanbanboard.backend.dto.MoveCardRequest;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.dto.UpdateCardRequest;
import com.kanbanboard.backend.service.CardService;

@RestController
@RequestMapping("/api/board")
public class CardController {

    private final CardService cardService;
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/columns/{columnId}/cards")
    public ResponseEntity<Response<CardDTO>> createCard(@PathVariable UUID columnId, @RequestBody CreateCardRequest request, Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<CardDTO> res = cardService.createCard(columnId, request, userId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<Response<String>> deleteCard(@PathVariable UUID cardId, Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<String> res = cardService.deleteCard(cardId, userId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @PutMapping("/cards/{cardId}")
    public ResponseEntity<Response<CardDTO>> updateCard(@PathVariable UUID cardId, @RequestBody UpdateCardRequest request, Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<CardDTO> res = cardService.updateCard(cardId, request, userId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @PutMapping("/cards/{cardId}/move")
    public ResponseEntity<Response<CardDTO>> moveCard(@PathVariable UUID cardId, @RequestBody MoveCardRequest request, Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<CardDTO> res = cardService.moveCard(cardId, request, userId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }
}