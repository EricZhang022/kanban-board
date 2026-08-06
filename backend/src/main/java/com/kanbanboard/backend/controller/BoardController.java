package com.kanbanboard.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    
    @PostMapping("/create")
    public ResponseEntity<Response<BoardDTO>> createBoard(@RequestBody CreateBoardRequest request, Authentication auth) {
        String owner = auth.getName();
        Response<BoardDTO> res = boardService.createBoard(request, owner);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }
}
