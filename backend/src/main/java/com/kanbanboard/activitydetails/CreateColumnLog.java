package com.kanbanboard.activitydetails;

import java.util.UUID;

public class CreateColumnLog implements ActivityDetails {
    public String columnName;

    public CreateColumnLog(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

   


}
