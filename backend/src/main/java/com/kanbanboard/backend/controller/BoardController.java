package com.kanbanboard.backend.controller;

import java.util.List;
import java.util.UUID;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PutMapping;

import com.kanbanboard.backend.dto.BoardDTO;
import com.kanbanboard.backend.dto.CardDTO;
import com.kanbanboard.backend.dto.ColumnDTO;
import com.kanbanboard.backend.dto.CreateBoardRequest;
import com.kanbanboard.backend.dto.CreateCardRequest;
import com.kanbanboard.backend.dto.CreateColumnRequest;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.dto.UpdateBoardNameRequest;
import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.service.BoardService;
import com.kanbanboard.backend.entity.Column;
import com.kanbanboard.backend.entity.Card;
import com.kanbanboard.backend.repo.BoardColumnRepository;
import com.kanbanboard.backend.repo.BoardRepository;
import com.kanbanboard.backend.repo.CardRepository;
import com.kanbanboard.backend.dto.ReorderColumnsRequest;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final CardRepository cardRepository;
    public BoardController(BoardService boardService, BoardRepository boardRepository, BoardColumnRepository boardColumnRepository, CardRepository cardRepository) {
        this.boardService = boardService;
        this.boardRepository = boardRepository;
        this.boardColumnRepository = boardColumnRepository;
        this.cardRepository = cardRepository;

    }
    @GetMapping
    public ResponseEntity<Response<List<BoardDTO>>> fetchAllBoards(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<List<BoardDTO>> res = boardService.getAllBoards(userId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @PostMapping("/create")
    public ResponseEntity<Response<BoardDTO>> createBoard(@RequestBody CreateBoardRequest request, Authentication auth) {
        UUID ownerId = UUID.fromString(auth.getName());
        Response<BoardDTO> res = boardService.createBoard(request, ownerId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<BoardDTO>> fetchOneBoard(@PathVariable("id") UUID boardId, Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<BoardDTO> res = boardService.getOneBoard(boardId, userId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response<BoardDTO>> updateBoardName(@PathVariable("id") UUID boardId, @RequestBody UpdateBoardNameRequest request, Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<BoardDTO> res = boardService.changeBoardName(boardId, userId, request.getBoardName());
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deleteBoard(@PathVariable("id") UUID boardId, Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<String> res = boardService.deleteABoard(boardId, userId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @PostMapping("/{id}/columns")
    public ResponseEntity<?> addColumn(@PathVariable UUID id, @RequestBody CreateColumnRequest request) {
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));

        Column column = new Column();
        column.setName(request.getName());
        column.setPosition(board.getColumns() != null ? board.getColumns().size() : 0);
        column.setBoard(board);

        boardColumnRepository.save(column);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "message", "Column created successfully",
            "data", new ColumnDTO(column)
        ));
    } 
    @DeleteMapping("/columns/{columnId}")
    public ResponseEntity<?> deleteColumn(@PathVariable UUID columnId) {
        if (!boardColumnRepository.existsById(columnId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found");
        }

        boardColumnRepository.deleteById(columnId);

        return ResponseEntity.ok(Map.of("message", "Column deleted successfully"));
    }
    @PutMapping("/{id}/columns/reorder")
    public ResponseEntity<?> reorderColumns(@PathVariable UUID id, @RequestBody ReorderColumnsRequest request) {
        List<UUID> orderedIds = request.getColumnIds();
        for (int i = 0; i < orderedIds.size(); i++) {
            UUID colId = orderedIds.get(i);
            Column col = boardColumnRepository.findById(colId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found"));
            col.setPosition(i);
            boardColumnRepository.save(col);
        }
        return ResponseEntity.ok(Map.of("message", "Columns reordered successfully"));
    }
    @PostMapping("/columns/{columnId}/cards")
    public ResponseEntity<?> addCard(@PathVariable UUID columnId,@RequestBody CreateCardRequest request) {
        Column column = boardColumnRepository.findById(columnId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found"));

        Card card = new Card();
        card.setTitle(request.getTitle());
        card.setDescription(request.getDescription());
        card.setPosition(column.getCards() != null ? column.getCards().size() : 0);
        card.setColumn(column);

        cardRepository.save(card);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "message", "Card created successfully",
            "data", new CardDTO(card)
        ));
    }
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable UUID cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found");
        }
        cardRepository.deleteById(cardId);
        return ResponseEntity.ok(Map.of("message", "Card deleted successfully"));
    }
    
    @PutMapping("/cards/{cardId}")
    public ResponseEntity<?> updateCard(@PathVariable UUID cardId, @RequestBody com.kanbanboard.backend.dto.UpdateCardRequest request) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found"));

        card.setTitle(request.getTitle());
        card.setDescription(request.getDescription());
        cardRepository.save(card);

        return ResponseEntity.ok(Map.of("message", "Card updated successfully"));
    }

    @PutMapping("/cards/{cardId}/move")
    public ResponseEntity<?> moveCard(@PathVariable UUID cardId, @RequestBody com.kanbanboard.backend.dto.MoveCardRequest request) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found"));

        Column targetCol = boardColumnRepository.findById(request.getTargetColumnId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target column not found"));

        card.setColumn(targetCol);
        card.setPosition(request.getNewPosition());
        cardRepository.save(card);

        return ResponseEntity.ok(Map.of("message", "Card moved successfully"));
    }
}

