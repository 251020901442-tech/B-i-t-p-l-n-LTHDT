package com.example.demo;
import jakarta.persistence.*;

@Entity 
@Table(name="physical_books")
@PrimaryKeyJoinColumn (name="book_id") // lien ket khoa chinh voi bang cha

public class PhysicalBook extends Book{
    private double weight;
    private String dimension;
    private int stockQuantity;
    private String coverType;
    
    // getter
    public double getWeight(){
        return weight;
    }
    public String getDimension(){
        return dimension;
    }
    public int getStockQuantity(){
        return stockQuantity;
    }
    public String getCoverType(){
        return coverType;
    }

    // setter
    public void setWeight(double weight){
        this.weight=weight;
    }
    public void setDimension(String dimension){
        this.dimension=dimension;
    }
    public void setStockQuantity(int stockQuantity){
        this.stockQuantity=stockQuantity;
    }
    public void setCoverType(String coverType){
        this.coverType=coverType;
    }
}