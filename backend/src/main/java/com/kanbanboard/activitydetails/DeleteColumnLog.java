package com.kanbanboard.activitydetails;

public class DeleteColumnLog implements ActivityDetails{
    public String columnName;

    public DeleteColumnLog(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
