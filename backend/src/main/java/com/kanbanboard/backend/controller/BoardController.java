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

import com.kanbanboard.backend.dto.BoardDTO;
import com.kanbanboard.backend.dto.ColumnDTO;
import com.kanbanboard.backend.dto.CreateBoardRequest;
import com.kanbanboard.backend.dto.CreateColumnRequest;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.dto.UpdateBoardNameRequest;
import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.service.BoardService;
import com.kanbanboard.backend.entity.Column;
import com.kanbanboard.backend.repo.BoardColumnRepository;
import com.kanbanboard.backend.repo.BoardRepository;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final BoardColumnRepository boardColumnRepository;
    public BoardController(BoardService boardService, BoardRepository boardRepository, BoardColumnRepository boardColumnRepository) {
        this.boardService = boardService;
        this.boardRepository = boardRepository;
        this.boardColumnRepository = boardColumnRepository;

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
}

