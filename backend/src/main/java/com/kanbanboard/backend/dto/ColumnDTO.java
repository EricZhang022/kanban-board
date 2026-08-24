package com.kanbanboard.backend.dto;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import com.kanbanboard.backend.entity.Column;
import com.kanbanboard.backend.entity.Card;


public class ColumnDTO {
    private UUID columnId;
    private String name;
    private int position;
    private List<CardDTO> cards;

    public ColumnDTO(Column column) {
        this.columnId = column.getColumnId();
        this.name = column.getName();
        this.position = column.getPosition();
        this.cards = new ArrayList<>();
        if (column.getCards() != null) {
            for (Card card : column.getCards()) {
                this.cards.add(new CardDTO(card));
            }
        }
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
    public List<CardDTO> getCards() {
         return cards; 
    }
}