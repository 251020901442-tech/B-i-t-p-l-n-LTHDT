package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity 
@Table (name="Books")
@Inheritance (strategy= InheritanceType.JOINED) // chien luoc tao bang cha - con rieng biet


public class Book{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int bookId;
    private String title;
    private String author;
    private double price;
    private String description;
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "category")
    private String category;

    // getter 
    public int getBookId(){
        return bookId;
    }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public double getPrice(){
        return price;
    }
    public String getDescription(){
        return description;
    }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
    // setter
    public void setBookId(int bookId){
        this.bookId=bookId;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public void setAuthor(String author){
        this.author=author;
    }
    public void setPrice(double price){
        this.price=price;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCategory(String category) { this.category = category; }

}