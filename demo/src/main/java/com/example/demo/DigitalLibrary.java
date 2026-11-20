package com.example.demo;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;


@Entity 
@Table (name="digital_library")
public class DigitalLibrary {
    @Id 
    @GeneratedValue (strategy =GenerationType.IDENTITY)
    private int libaryId;

    // lien ket 1-1 void user ( moi nguoi dung 1 tu sach)
    @OneToOne 
    @JoinColumn (name="user_id")
    private User owner;
    // lien ket N-N : mot tu sach co nhieu e-book
    @ManyToMany
    @JoinTable (
        name="library_ebook",
        joinColumns =@JoinColumn(name="library_id"),
        inverseJoinColumns=@JoinColumn(name="ebook_id")
    )
    private List<EBook> purchasedEBooks;
    private Date lastAccessed;
}
