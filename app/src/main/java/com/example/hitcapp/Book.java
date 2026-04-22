package com.example.hitcapp;

public class Book {
    private String title;
    private String author;
    private String price;
    private int imageResource;
    private String category; // Thêm trường thể loại

    public Book(String title, String author, String price, int imageResource, String category) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.imageResource = imageResource;
        this.category = category;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPrice() { return price; }
    public int getImageResource() { return imageResource; }
    public String getCategory() { return category; }
}
