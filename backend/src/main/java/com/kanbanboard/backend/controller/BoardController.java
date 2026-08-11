package com.kanbanboard.backend.controller;

import java.util.List;
import java.util.UUID;

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

import com.kanbanboard.backend.dto.BoardDTO;
import com.kanbanboard.backend.dto.CreateBoardRequest;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.dto.UpdateBoardNameRequest;
import com.kanbanboard.backend.service.BoardService;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
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
    public ResponseEntity<Response<BoardDTO>> fecthOneBoard(@PathVariable("id") UUID boardId, Authentication auth) {
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
}
