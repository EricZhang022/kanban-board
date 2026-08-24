package com.kanbanboard.backend.dto;

import java.util.UUID;
import com.kanbanboard.backend.entity.Card;

public class CardDTO {
    private UUID cardId;
    private String title;
    private String description;
    private int position;

    public CardDTO() {}

    public CardDTO(Card card) {
        this.cardId = card.getCardId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.position = card.getPosition();
    }

    public UUID getCardId() { return cardId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getPosition() { return position; }
}