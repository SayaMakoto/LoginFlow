package com.example.hitcapp;

public class Game {
    private String name;
    private int imageResource;

    public Game(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() { return name; }
    public int getImageResource() { return imageResource; }
}