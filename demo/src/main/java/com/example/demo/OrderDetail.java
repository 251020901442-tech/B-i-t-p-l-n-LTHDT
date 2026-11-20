package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detailId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // Trỏ đến lớp cha Book (để có thể là PhysicalBook hoặc EBook)
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private int quantity;
    private double unitPrice;

    // Generate Getters and Setters
}