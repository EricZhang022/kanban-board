package com.kanbanboard.activitydetails;

public class UpdateCardLog implements ActivityDetails{
    public String cardName;
    public String columnName;

    
    public String getCardName() {
        return cardName;
    }
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public UpdateCardLog(String cardName, String columnName) {
        this.cardName = cardName;
        this.columnName = columnName;
    }
}
