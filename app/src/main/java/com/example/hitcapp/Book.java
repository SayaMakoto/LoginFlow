package com.example.hitcapp;

public class Book {
    private String title;
    private String author;
    private String price;
    private int imageResource;

    public Book(String title, String author, String price, int imageResource) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.imageResource = imageResource;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPrice() { return price; }
    public int getImageResource() { return imageResource; }
}
