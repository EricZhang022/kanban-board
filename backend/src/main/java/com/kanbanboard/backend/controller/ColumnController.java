package com.kanbanboard.backend.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.kanbanboard.backend.dto.ColumnDTO;
import com.kanbanboard.backend.dto.CreateColumnRequest;
import com.kanbanboard.backend.dto.ReorderColumnsRequest;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.service.ColumnService;

@RestController
@RequestMapping("/api/board")
public class ColumnController {

    private final ColumnService columnService;
    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    @PostMapping("/{id}/columns")
    public ResponseEntity<Response<ColumnDTO>> createColumn(@PathVariable("id") UUID boardId, @RequestBody CreateColumnRequest request, Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<ColumnDTO> res = columnService.createColumn(boardId, request, userId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @DeleteMapping("/columns/{columnId}")
    public ResponseEntity<Response<String>> deleteColumn(@PathVariable UUID columnId, Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<String> res = columnService.deleteColumn(columnId, userId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @PutMapping("/{id}/columns/reorder")
    public ResponseEntity<Response<String>> reorderColumns(@PathVariable("id") UUID boardId, @RequestBody ReorderColumnsRequest request, Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        Response<String> res = columnService.reorderColumns(boardId, request, userId);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }
}