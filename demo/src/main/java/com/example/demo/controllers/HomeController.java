package com.example.demo.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import com.example.demo.CartItem;
import com.example.demo.Book;
import com.example.demo.OrderHistory;
import com.example.demo.User;
import com.example.demo.repository.OrderHistoryRepository;
import com.example.demo.services.BookService;
import com.example.demo.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;
    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    // ==========================================
    // 1. CÁC TRANG LIÊN QUAN ĐẾN SÁCH (TRANG CHỦ)
    // ==========================================

    @GetMapping("/")
    public String homePage(Model model) {
        List<Book> allBooks = bookService.getAllBooks();
        model.addAttribute("books", allBooks);
        return "home";
    }

    @GetMapping("/search")
    public String searchPage(@RequestParam("keyword") String keyword, Model model) {
        List<Book> searchResults = bookService.searchBooks(keyword);
        model.addAttribute("books", searchResults);
        model.addAttribute("keyword", keyword);
        return "home";
    }

    // ==========================================
    // 2. CHỨC NĂNG ĐĂNG NHẬP & ĐĂNG XUẤT
    // ==========================================

    // Mở giao diện Đăng nhập
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Xử lý dữ liệu khi bấm nút "Đăng Nhập"
    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username, 
                               @RequestParam("password") String password, 
                               HttpSession session, 
                               Model model) {
        
        User user = userService.login(username, password);
        
        if (user != null) {
            // Lưu thông tin người dùng vào Session
            session.setAttribute("loggedInUser", user);

            // Phân luồng: Nhân viên vào trang duyệt đơn, Khách hàng vào Profile
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin/orders";
            }
            return "redirect:/profile";
        } else {
            model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
            return "login"; 
        }
    }

    // Xử lý khi bấm nút "Đăng xuất"
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Xóa thông tin tài khoản khỏi bộ nhớ
        session.removeAttribute("loggedInUser");
        // Nhảy về trang chủ
        return "redirect:/";
    }

    // ==========================================
    // 3. CHỨC NĂNG ĐĂNG KÝ TÀI KHOẢN MỚI
    // ==========================================

    // Mở giao diện Đăng ký
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // Xử lý dữ liệu khi bấm nút "Tạo Tài Khoản"
    @PostMapping("/register")
    public String processRegister(@RequestParam("username") String username, 
                                  @RequestParam("password") String password,
                                  @RequestParam("confirmPassword") String confirmPassword,
                                  Model model) {
        
        // Kiểm tra mật khẩu nhập lại có khớp không
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu nhập lại không khớp!");
            return "register";
        }
        
        // Gọi hàm lưu vào CSDL (hàm register nằm trong UserService)
        boolean isSuccess = userService.register(username, password);
        
        if (isSuccess) {
            // Đăng ký thành công -> Nhảy thẳng sang trang Đăng nhập
            return "redirect:/login"; 
        } else {
            // Nếu username đã có người lấy, báo lỗi và ở lại trang đăng ký
            model.addAttribute("error", "Tên đăng nhập đã tồn tại, vui lòng chọn tên khác!");
            return "register";
        }
    }
    // ==========================================
    // 4. TRANG PROFILE KHÁCH HÀNG & THỨ HẠNG
    // ==========================================
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Nếu chưa đăng nhập thì bắt quay lại trang login
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Lấy lịch sử mua hàng của đúng tài khoản đang đăng nhập
       List<OrderHistory> orders = orderHistoryRepository.findByUserIdAndDeletedFalseOrderByIdDesc(loggedInUser.getId());

        // Tính tổng số quyển sách khách đã mua
        int totalBooks = 0;
        for (OrderHistory order : orders) {
           if ("Hoàn thành".equals(order.getStatus()) || "Đã kích hoạt Ebook".equals(order.getStatus())) {
                totalBooks += order.getQuantity();
            }
        }

        // Xét thứ hạng tài khoản theo số lượng sách đã mua
        String rankName;
        String rankBadgeClass;
        String nextRankNote;

        if (totalBooks >= 11) {
            rankName = "Hạng Kim Cương";
            rankBadgeClass = "bg-info text-dark";
            nextRankNote = "Bạn đã đạt thứ hạng cao nhất!";
        } else if (totalBooks >= 6) {
            rankName = "Hạng Vàng";
            rankBadgeClass = "bg-warning text-dark";
            nextRankNote = "Mua thêm " + (11 - totalBooks) + " quyển sách để lên Hạng Kim Cương";
        } else if (totalBooks >= 3) {
            rankName = "Hạng Bạc";
            rankBadgeClass = "bg-secondary text-white";
            nextRankNote = "Mua thêm " + (6 - totalBooks) + " quyển sách để lên Hạng Vàng";
        } else {
            rankName = "Hạng Đồng";
            rankBadgeClass = "bg-danger text-white";
            nextRankNote = "Mua thêm " + (3 - totalBooks) + " quyển sách để lên Hạng Bạc";
        }

        model.addAttribute("user", loggedInUser);
        model.addAttribute("orders", orders);
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("rankName", rankName);
        model.addAttribute("rankBadgeClass", rankBadgeClass);
        model.addAttribute("nextRankNote", nextRankNote);

        return "profile";
    }
    // ==========================================
    // 5. GIỎ HÀNG & THANH TOÁN (CHỌN EBOOK / BOOK)
    // ==========================================

    // Thêm sách vào giỏ hàng
   // Thêm sách vào giỏ hàng (AJAX - trả về JSON, không load lại trang)
    @PostMapping("/api/cart/add")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String, Object> addToCart(@RequestParam("title") String title,
                                         @RequestParam("price") double price,
                                         HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Nếu sách đã có trong giỏ thì tăng số lượng lên 1
        boolean found = false;
        for (CartItem item : cart) {
            if (item.getTitle().equals(title)) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }
        if (!found) {
            cart.add(new CartItem(title, price, 1));
        }

        // Tính tổng số lượng sách để hiển thị trên icon Giỏ hàng
        int totalQuantity = 0;
        for (CartItem item : cart) {
            totalQuantity += item.getQuantity();
        }

        session.setAttribute("cart", cart);
        session.setAttribute("cartCount", totalQuantity);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("cartCount", totalQuantity);
        return response;
    }

    // Xem trang giỏ hàng
    @GetMapping("/cart")
    @SuppressWarnings("unchecked")
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        double totalBookPrice = 0;
        double totalEbookPrice = 0;
        for (CartItem item : cart) {
            totalBookPrice += item.getBookSubtotal();
            totalEbookPrice += item.getEbookSubtotal();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("totalBookPrice", totalBookPrice);
        model.addAttribute("totalEbookPrice", totalEbookPrice);

        return "cart";
    }

    // Xóa 1 sách khỏi giỏ hàng
    @GetMapping("/cart/remove")
    @SuppressWarnings("unchecked")
    public String removeFromCart(@RequestParam("index") int index, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null && index >= 0 && index < cart.size()) {
            cart.remove(index);
            int totalQuantity = 0;
            for (CartItem item : cart) {
                totalQuantity += item.getQuantity();
            }
            session.setAttribute("cart", cart);
            session.setAttribute("cartCount", totalQuantity);
        }
        return "redirect:/cart";
    }

    // Xử lý xác nhận thanh toán (lưu vào bảng order_history trong MySQL)
   // Xử lý xác nhận thanh toán (lưu vào bảng order_history trong MySQL)
    @PostMapping("/checkout")
    @SuppressWarnings("unchecked")
    public String processCheckout(
            @RequestParam("bookType") String bookType,
            @RequestParam(value = "recipientName", required = false) String recipientName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "email", required = false) String email,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null && !cart.isEmpty()) {
            for (CartItem item : cart) {
                OrderHistory order = new OrderHistory();
                order.setUser(loggedInUser);
                order.setQuantity(item.getQuantity());

                if ("EBOOK".equals(bookType)) {
                    order.setBookTitle(item.getTitle() + " (Ebook)");
                    order.setTotalPrice(item.getEbookSubtotal());
                    order.setStatus("Chờ duyệt thanh toán");
                } else {
                    order.setBookTitle(item.getTitle() + " (Sách giấy)");
                    order.setTotalPrice(item.getBookSubtotal());
                    order.setRecipientName(recipientName);
                    order.setPhone(phone);
                    order.setAddress(address);
                    order.setEmail(email);
                    order.setStatus("Chờ giao hàng");
                }

                orderHistoryRepository.save(order);
            }

            session.removeAttribute("cart");
            session.setAttribute("cartCount", 0);
        }

        return "redirect:/profile";
    }
    // Khách hàng hủy đơn (chỉ được hủy khi nhân viên chưa xử lý)
    @PostMapping("/order/cancel")
    public String cancelOrder(@RequestParam("orderId") int orderId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        OrderHistory order = orderHistoryRepository.findById(orderId).orElse(null);
        if (order != null && order.getUser().getId() == loggedInUser.getId()) {
            // Cho phép hủy mọi đơn có trạng thái bắt đầu bằng "chờ" (Chờ giao hàng, Chờ xác nhận, Chờ duyệt...)
            if (order.getStatus() != null && order.getStatus().trim().toLowerCase().startsWith("chờ")) {
                order.setStatus("Đã hủy");
                orderHistoryRepository.save(order);
            }
        }

        return "redirect:/profile";
    }
}