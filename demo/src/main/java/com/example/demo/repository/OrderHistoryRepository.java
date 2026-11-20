package com.example.demo.repository;

import com.example.demo.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Integer> {
    // Lấy đơn chưa bị xóa của 1 khách hàng (cho trang Profile)
    List<OrderHistory> findByUserIdAndDeletedFalseOrderByIdDesc(int userId);

    // Lấy toàn bộ đơn đang hoạt động (cho trang chính của Admin)
    List<OrderHistory> findByDeletedFalseOrderByIdDesc();

    // Lấy toàn bộ đơn trong Thùng rác, mới xóa lên đầu (cho Admin)
    List<OrderHistory> findByDeletedTrueOrderByDeletedAtDesc();
}