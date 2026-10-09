package com.kanbanboard.activitydetails;

public class DeleteCardLog implements ActivityDetails{
    public String cardName;

    
    public DeleteCardLog(String cardName) {
        this.cardName = cardName;
    }
    public String getCardName() {
        return cardName;
    }
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

}
