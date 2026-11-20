package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.OrderHistory;
import com.example.demo.User;
import com.example.demo.repository.OrderHistoryRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    // 1. Xem danh sách đơn hàng chính hoặc Thùng rác (?view=trash)
    @GetMapping("/orders")
    public String manageOrders(
            @RequestParam(value = "view", defaultValue = "active") String view,
            HttpSession session,
            Model model) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        if (!isAdmin(session)) {
            return "redirect:/";
        }

        boolean isTrashView = "trash".equalsIgnoreCase(view);
        List<OrderHistory> activeOrders = orderHistoryRepository.findByDeletedFalseOrderByIdDesc();
        List<OrderHistory> trashOrders = orderHistoryRepository.findByDeletedTrueOrderByDeletedAtDesc();

        model.addAttribute("orders", isTrashView ? trashOrders : activeOrders);
        model.addAttribute("isTrashView", isTrashView);
        model.addAttribute("activeCount", activeOrders.size());
        model.addAttribute("trashCount", trashOrders.size());
        return "admin-orders";
    }

    // 2. Cập nhật trạng thái đơn hàng và link Ebook
    @PostMapping("/orders/update")
    public String updateOrderStatus(
            @RequestParam("orderId") int orderId,
            @RequestParam("status") String status,
            @RequestParam(value = "downloadLink", required = false) String downloadLink,
            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }

        OrderHistory order = orderHistoryRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            if (downloadLink != null && !downloadLink.trim().isEmpty()) {
                order.setDownloadLink(downloadLink.trim());
            }
            orderHistoryRepository.save(order);
        }

        return "redirect:/admin/orders";
    }

    // 3. Xóa mềm đơn hàng (lưu tên nhân viên xóa và thời gian xóa)
    @PostMapping("/orders/delete")
    public String softDeleteOrder(@RequestParam("orderId") int orderId, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }

        User admin = (User) session.getAttribute("loggedInUser");
        OrderHistory order = orderHistoryRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setDeleted(true);
            order.setDeletedBy(admin.getUsername());
            order.setDeletedAt(LocalDateTime.now());
            orderHistoryRepository.save(order);
        }

        return "redirect:/admin/orders";
    }

    // 4. Khôi phục đơn hàng từ Thùng rác
    @PostMapping("/orders/restore")
    public String restoreOrder(@RequestParam("orderId") int orderId, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }

        OrderHistory order = orderHistoryRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setDeleted(false);
            order.setDeletedBy(null);
            order.setDeletedAt(null);
            orderHistoryRepository.save(order);
        }

        return "redirect:/admin/orders?view=trash";
    }
}