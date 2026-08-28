package com.kanbanboard.activitydetails;

import java.util.UUID;


public class createCardLog implements ActivityDetails{
    public UUID cardID;
    public UUID columnID;

    public UUID getCardID() {
        return cardID;
    }

    public void setCardID(UUID cardID) {
        this.cardID = cardID;
    }

    public UUID getColumnID() {
        return columnID;
    }

    public void setColumnID(UUID columnID) {
        this.columnID = columnID;
    }

    public createCardLog(UUID cardID, UUID columnID){
        this.cardID = cardID;
        this.columnID = columnID;
    }
}
