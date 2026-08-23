package com.kanbanboard.backend.dto;

import java.util.List;
import java.util.UUID;

public class ReorderColumnsRequest {
    private List<UUID> columnIds;

    public ReorderColumnsRequest() {}

    public List<UUID> getColumnIds() {
        return columnIds;
    }

    public void setColumnIds(List<UUID> columnIds) {
        this.columnIds = columnIds;
    }
}