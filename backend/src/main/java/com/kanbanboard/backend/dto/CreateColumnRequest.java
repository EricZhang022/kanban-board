package com.kanbanboard.backend.dto;

public class CreateColumnRequest {
    private String name;

    public CreateColumnRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}