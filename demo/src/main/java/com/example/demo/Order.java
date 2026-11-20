package com.example.demo;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    private int orderId;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User customer;
    private Date orderDate;
    private double totalAmount;
    private String status;// pending ,paid, shipped ,completed
    // lien ket 1-N voi chi tiet don hang 
    @OneToMany (mappedBy="order", cascade=CascadeType.ALL)
    private List<OrderDetail> orderDerDetails;
}
