package com.kanbanboard.backend.dto;

import java.util.UUID;
import com.kanbanboard.backend.entity.Column;

public class ColumnDTO {
    private UUID columnId;
    private String name;
    private int position;

    public ColumnDTO(Column column) {
        this.columnId = column.getColumnId();
        this.name = column.getName();
        this.position = column.getPosition();
    }

    public UUID getColumnId() {
        return columnId;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }
}