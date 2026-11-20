package com.example.demo;

import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        // Kiểm tra nếu bảng sách chưa có dữ liệu thì mới thêm vào
        if (bookRepository.count() == 0) {
            
            // 1. Tạo một quyển sách vật lý (PhysicalBook)
            PhysicalBook book1 = new PhysicalBook();
            book1.setTitle("Lập trình C và Cấu trúc dữ liệu");
            book1.setAuthor("Nguyễn Trọng Bảo");
            book1.setPrice(150000);
            book1.setDescription("Sách hướng dẫn giải thuật và lập trình C từ cơ bản đến nâng cao.");
            book1.setWeight(0.5);
            book1.setDimension("15x20 cm");
            book1.setStockQuantity(50);
            book1.setCoverType("Bìa mềm");

            // 2. Tạo một quyển sách điện tử (EBook)
            EBook ebook1 = new EBook();
            ebook1.setTitle("Thực hành Bảo mật mạng với Kali Linux");
            ebook1.setAuthor("Phenikaa IT");
            ebook1.setPrice(99000);
            ebook1.setDescription("Tài liệu hướng dẫn Penetration Testing, Nmap và Metasploit trên máy ảo.");
            ebook1.setFileFormat("PDF");
            ebook1.setFileSize(15.5);
            ebook1.setDownloadLink("https://example.com/kalilinux.pdf");
            ebook1.setDeviceLimit(3);

            // 3. Tạo thêm một sách Ebook nữa để giao diện đẹp hơn
            EBook ebook2 = new EBook();
            ebook2.setTitle("Luyện thi TOEIC (Part 1, 2, 4, 5, 6)");
            ebook2.setAuthor("Trung tâm Ngoại ngữ");
            ebook2.setPrice(85000);
            ebook2.setDescription("Tổng hợp các đề thi TOEIC Reading và Listening sát với thực tế.");
            ebook2.setFileFormat("EPUB");
            ebook2.setFileSize(20.0);
            ebook2.setDownloadLink("https://example.com/toeic.epub");
            ebook2.setDeviceLimit(2);

            // Lưu tất cả vào Cơ sở dữ liệu
            bookRepository.save(book1);
            bookRepository.save(ebook1);
            bookRepository.save(ebook2);

            System.out.println("✅ Đã thêm dữ liệu sách mẫu thành công!");
        }
    }
}