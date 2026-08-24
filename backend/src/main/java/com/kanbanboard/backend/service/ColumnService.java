package com.kanbanboard.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kanbanboard.backend.dto.ColumnDTO;
import com.kanbanboard.backend.dto.CreateColumnRequest;
import com.kanbanboard.backend.dto.ReorderColumnsRequest;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.Column;
import com.kanbanboard.backend.repo.BoardColumnRepository;
import com.kanbanboard.backend.repo.BoardRepository;

@Service
public class ColumnService {
    private final BoardRepository boardRepo;
    private final BoardColumnRepository columnRepo;
    private final BoardService boardService;

    public ColumnService(BoardRepository boardRepo, BoardColumnRepository columnRepo, BoardService boardService) {
        this.boardRepo = boardRepo;
        this.columnRepo = columnRepo;
        this.boardService = boardService;
    }

    public Response<ColumnDTO> createColumn(UUID boardId, CreateColumnRequest request, UUID userId) {
        Response<ColumnDTO> res;
        Board board;

        try {
            board = boardRepo.findById(boardId)
                .orElseThrow(() -> new RuntimeException("To create a column, the board need to exist"));
        }
        catch (RuntimeException e) {
            res = new Response<>(404, e.getMessage());
            return res;
        }

        boolean doUserHasAccess = boardService.hasAccess(board, userId);
        if (!doUserHasAccess) {
            res = new Response<>(403, "You do not have access to create column for this board");
            return res;
        }

        String columnName = request.getName();
        int pos = board.getColumns() != null ? board.getColumns().size() : 0;
        
        Column column = new Column(columnName, pos, board);
        Column savedColumn = columnRepo.save(column);

        ColumnDTO colDTO = new ColumnDTO(savedColumn);
        res = new Response<>(200, "Column created successfully", colDTO);
        return res;
    }

        public Response<String> deleteColumn(UUID columnId,  UUID userId) {
        Response<String> res;
        Board board;
        Column col;

        try {
            col = columnRepo.findById(columnId)
                .orElseThrow(() -> new RuntimeException("This column does not exist"));
        }
        catch (RuntimeException e) {
            res = new Response<>(404, e.getMessage());
            return res;
        }
        board = col.getBoard();
        if (board == null) {
            res = new Response<>(404, "Board not found for deleting a column");
            return res;
        }

        boolean doUserHasAccess = boardService.hasAccess(board, userId);
        if (!doUserHasAccess) {
            res = new Response<>(403, "You do not have access to delete this column for this board");
            return res;
        }

        columnRepo.deleteById(columnId);
        res = new Response<>(200, "Column deleted successfully");
        return res;
    }

    public Response<String> reorderColumns(UUID boardId, ReorderColumnsRequest request, UUID userId) {
        Response<String> res;
        Board board;

        try {
            board = boardRepo.findById(boardId)
                .orElseThrow(() -> new RuntimeException("To reorder columns, the board need to exist"));
        }
        catch (RuntimeException e) {
            res = new Response<>(404, e.getMessage());
            return res;
        }

        boolean doUserHasAccess = boardService.hasAccess(board, userId);
        if (!doUserHasAccess) {
            res = new Response<>(403, "You do not have access to reorder columns for this board");
            return res;
        }
        List<UUID> orderedIds = request.getColumnIds();
        List<Column> columnsToUpdate = new ArrayList<>();

        // validate everything before changing anything
        for (UUID colId : orderedIds) {
            Column col;
            try {
                col = columnRepo.findById(colId)
                    .orElseThrow(() -> new RuntimeException("Column not found: " + colId));
            } catch (RuntimeException e) {
                res = new Response<>(404, e.getMessage());
                return res;
            }

            if (!col.getBoard().getBoardId().equals(boardId)) {
                res = new Response<>(400, "Column does not belong to this board: " + colId);
                return res;
            }

            columnsToUpdate.add(col);
        }

        // apply changes
        for (int i = 0; i < columnsToUpdate.size(); i++) {
            columnsToUpdate.get(i).setPosition(i);
            columnRepo.save(columnsToUpdate.get(i));
        }
        res = new Response<>(200, "Columns reordered successfully");
        return res;
    }
    
}
