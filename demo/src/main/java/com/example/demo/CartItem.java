package com.example.demo;

public class CartItem {
    private String title;
    private double price; // Giá sách giấy (giá gốc)
    private int quantity;

    public CartItem(String title, double price, int quantity) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    // Giá 1 cuốn Ebook = 60% giá sách giấy (bạn có thể đổi 0.6 thành tỉ lệ khác)
    public double getEbookPrice() {
        return Math.round(this.price * 0.6);
    }

    // Thành tiền nếu mua Sách giấy
    public double getBookSubtotal() {
        return this.price * this.quantity;
    }

    // Thành tiền nếu mua Ebook
    public double getEbookSubtotal() {
        return getEbookPrice() * this.quantity;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}