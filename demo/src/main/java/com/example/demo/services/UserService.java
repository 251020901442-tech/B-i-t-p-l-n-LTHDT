package com.example.demo.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // logic kiem tra dang nhap 
    public User login(String username,String password){
        User user = userRepository.findByUsername(username).orElse(null);
        if(user != null && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }
    // 2. Logic Đăng ký lưu vào MySQL
    public boolean register(String username, String password) {
        // Kiểm tra xem tên đăng nhập đã tồn tại trong CSDL chưa
        if (userRepository.existsByUsername(username)) {
            return false; // Đã tồn tại, từ chối đăng ký
        }
        
        // Tạo người dùng mới và lưu xuống MySQL
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // Tạm lưu mật khẩu thường
        newUser.setRole("USER");       // Cấp quyền mặc định là USER
        
        userRepository.save(newUser);  // Lệnh này tương đương INSERT INTO users... trong MySQL
        return true; 
    }
}
    

