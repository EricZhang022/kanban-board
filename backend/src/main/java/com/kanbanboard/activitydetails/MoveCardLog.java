package com.kanbanboard.activitydetails;

public class MoveCardLog implements ActivityDetails{
    public String cardName;
    public String columnFrom;
    public String columnTo;

    
    public String getCardName() {
        return cardName;
    }
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    public String getColumnFrom() {
        return columnFrom;
    }
    public void setColumnFrom(String columnFrom) {
        this.columnFrom = columnFrom;
    }
    public String getColumnTo() {
        return columnTo;
    }
    public void setColumnTo(String columnTo) {
        this.columnTo = columnTo;
    }
    public MoveCardLog(String cardName, String columnFrom, String columnTo) {
        this.cardName = cardName;
        this.columnFrom = columnFrom;
        this.columnTo = columnTo;
    }
}
