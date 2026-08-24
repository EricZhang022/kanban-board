package com.kanbanboard.backend.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kanbanboard.backend.dto.CardDTO;
import com.kanbanboard.backend.dto.CreateCardRequest;
import com.kanbanboard.backend.dto.MoveCardRequest;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.dto.UpdateCardRequest;
import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.Card;
import com.kanbanboard.backend.entity.Column;
import com.kanbanboard.backend.repo.BoardColumnRepository;
import com.kanbanboard.backend.repo.CardRepository;

@Service
public class CardService {
    private final BoardColumnRepository columnRepo;
    private final CardRepository cardRepo;
    private final BoardService boardService;

    public CardService(BoardColumnRepository columnRepo, CardRepository cardRepo, BoardService boardService) {
        this.columnRepo = columnRepo;
        this.cardRepo = cardRepo;
        this.boardService = boardService;
    }

    public Response<CardDTO> createCard(UUID columnId, CreateCardRequest request, UUID userId) {
        Response<CardDTO> res;
        Column column;

        try {
            column = columnRepo.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));
        } catch (RuntimeException e) {
            res = new Response<>(404, e.getMessage());
            return res;
        }

        Board board = column.getBoard();
        if (!boardService.hasAccess(board, userId)) {
            res = new Response<>(403, "You do not have access to create a card in this column");
            return res;
        }

        String title = request.getTitle();
        String description = request.getDescription();
        int pos = column.getCards() != null ? column.getCards().size() : 0;

        Card card = new Card(title, description, pos, column);
        Card savedCard = cardRepo.save(card);

        CardDTO cardDTO = new CardDTO(savedCard);
        res = new Response<>(200, "Card created successfully", cardDTO);
        return res;
    }

    public Response<String> deleteCard(UUID cardId, UUID userId) {
        Response<String> res;
        Card card;

        try {
            card = cardRepo.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        } catch (RuntimeException e) {
            res = new Response<>(404, e.getMessage());
            return res;
        }

        Board board = card.getColumn().getBoard();
        if (!boardService.hasAccess(board, userId)) {
            res = new Response<>(403, "You do not have access to delete this card");
            return res;
        }

        cardRepo.deleteById(cardId);
        res = new Response<>(200, "Card deleted successfully");
        return res;
    }

    public Response<CardDTO> updateCard(UUID cardId, UpdateCardRequest request, UUID userId) {
        Response<CardDTO> res;
        Card card;

        try {
            card = cardRepo.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        } catch (RuntimeException e) {
            res = new Response<>(404, e.getMessage());
            return res;
        }

        Board board = card.getColumn().getBoard();
        if (!boardService.hasAccess(board, userId)) {
            res = new Response<>(403, "You do not have access to update this card");
            return res;
        }

        card.setTitle(request.getTitle());
        card.setDescription(request.getDescription());
        Card savedCard = cardRepo.save(card);

        CardDTO cardDTO = new CardDTO(savedCard);
        res = new Response<>(200, "Card updated successfully", cardDTO);
        return res;
    }

    public Response<CardDTO> moveCard(UUID cardId, MoveCardRequest request, UUID userId) {
        Response<CardDTO> res;
        Card card;
        Column targetCol;

        try {
            card = cardRepo.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
            targetCol = columnRepo.findById(request.getTargetColumnId())
                .orElseThrow(() -> new RuntimeException("Target column not found"));
        } catch (RuntimeException e) {
            res = new Response<>(404, e.getMessage());
            return res;
        }

        Board originalBoard = card.getColumn().getBoard();
        Board targetBoard = targetCol.getBoard();

        if (!originalBoard.getBoardId().equals(targetBoard.getBoardId())) {
            res = new Response<>(400, "Cannot move a card to a column on a different board");
            return res;
        }

        if (!boardService.hasAccess(originalBoard, userId)) {
            res = new Response<>(403, "You do not have access to move this card");
            return res;
        }

        card.setColumn(targetCol);
        card.setPosition(request.getNewPosition());
        Card savedCard = cardRepo.save(card);

        CardDTO cardDTO = new CardDTO(savedCard);
        res = new Response<>(200, "Card moved successfully", cardDTO);
        return res;
    }
}