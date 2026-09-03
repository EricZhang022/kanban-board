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
import com.kanbanboard.backend.entity.ActivityLog.ActionType;
import com.kanbanboard.backend.repo.BoardColumnRepository;
import com.kanbanboard.backend.repo.CardRepository;
import com.kanbanboard.backend.repo.UserRepository;

import com.kanbanboard.activitydetails.CreateCardLog;
import com.kanbanboard.activitydetails.DeleteCardLog;
import com.kanbanboard.activitydetails.MoveCardLog;
import com.kanbanboard.activitydetails.UpdateCardLog;


@Service
public class CardService {
    private final BoardColumnRepository columnRepo;
    private final CardRepository cardRepo;
    private final BoardService boardService;
    private final ActivityLogService activityLogService;
    private final UserRepository userRepo;
    

    public CardService(BoardColumnRepository columnRepo, CardRepository cardRepo, BoardService boardService, ActivityLogService activityLogService, UserRepository userRepo) {
        this.columnRepo = columnRepo;
        this.cardRepo = cardRepo;
        this.boardService = boardService;
        this.activityLogService = activityLogService;
        this.userRepo = userRepo;
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


        CreateCardLog c = new CreateCardLog(title, columnRepo.getReferenceById(columnId).getName());
        activityLogService.logActivity(board, (userRepo.findById(userId)).orElse(null), ActionType.create_card, c);

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
        String cardName = card.getTitle();
        if (!boardService.hasAccess(board, userId)) {
            res = new Response<>(403, "You do not have access to delete this card");
            return res;
        }

        cardRepo.deleteById(cardId);

        DeleteCardLog d = new DeleteCardLog(cardName);
        activityLogService.logActivity(board, (userRepo.findById(userId)).orElse(null), ActionType.delete_card, d);


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

        UpdateCardLog u = new UpdateCardLog(request.getTitle(), card.getColumn().getName());
        activityLogService.logActivity(board, (userRepo.findById(userId)).orElse(null), ActionType.update_card, u);


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

        UpdateCardLog u = new UpdateCardLog(card.getTitle(), targetCol.getName());
        activityLogService.logActivity(originalBoard, (userRepo.findById(userId)).orElse(null), ActionType.move_card, u);


        res = new Response<>(200, "Card moved successfully", cardDTO);
        return res;
    }
}