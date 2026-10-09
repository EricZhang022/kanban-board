package com.kanbanboard.activitydetails;

import java.util.UUID;


public class CreateCardLog implements ActivityDetails{
    // public UUID cardID;
    // public UUID columnID;

    // public UUID getCardID() {
    //     return cardID;
    // }

    // public void setCardID(UUID cardID) {
    //     this.cardID = cardID;
    // }

    // public UUID getColumnID() {
    //     return columnID;
    // }

    // public void setColumnID(UUID columnID) {
    //     this.columnID = columnID;
    // }

    // public CreateCardLog(UUID cardID, UUID columnID){
    //     this.cardID = cardID;
    //     this.columnID = columnID;
    // }

    public String cardName;
    public String columnName;
    
    public CreateCardLog(String cardName, String columnName) {
        this.cardName = cardName;
        this.columnName = columnName;
    }
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


}
