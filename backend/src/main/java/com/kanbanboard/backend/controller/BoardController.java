package com.kanbanboard.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanbanboard.backend.dto.BoardDTO;
import com.kanbanboard.backend.dto.CreateBoardRequest;
import com.kanbanboard.backend.dto.Response;
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
        String username = auth.getName();
        Response<List<BoardDTO>> res = boardService.getAllBoards(username);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @PostMapping("/create")
    public ResponseEntity<Response<BoardDTO>> createBoard(@RequestBody CreateBoardRequest request, Authentication auth) {
        String owner = auth.getName();
        Response<BoardDTO> res = boardService.createBoard(request, owner);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<BoardDTO>> fecthOneBoard(@PathVariable("id") UUID boardId, Authentication auth) {
        String username = auth.getName();
        Response<BoardDTO> res = boardService.getOneBoard(boardId, username);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }
}
