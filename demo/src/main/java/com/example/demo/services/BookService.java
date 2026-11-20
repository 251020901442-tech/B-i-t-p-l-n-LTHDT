package com.example.demo.services;

import com.example.demo.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;

    // Lấy toàn bộ sách trong CSDL để hiển thị lên giao diện Web
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    // Tìm kiếm sách theo từ khóa
    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword);
    }
}