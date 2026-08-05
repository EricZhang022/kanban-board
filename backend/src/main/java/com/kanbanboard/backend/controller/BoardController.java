package com.kanbanboard.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanbanboard.backend.dto.BoardDTO;
import com.kanbanboard.backend.dto.CreateBoardRequest;
import com.kanbanboard.backend.dto.Response;

@RestController
@RequestMapping("/api/board")
public class BoardController {
    
    @PostMapping("/create")
    public ResponseEntity<Response<BoardDTO>> createBoard(@RequestBody CreateBoardRequest request) {
        
        ResponseEntity.ok(new Response<BoardDTO>(200, "Successfully created board"));
    }
}
