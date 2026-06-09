package api;

import api.dto.CheckoutRequest;
import model.Cart;
import model.Order;
import model.Student;
import org.springframework.web.bind.annotation.*;
import service.*;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private final OrderService orderService;
    private final CartService cartService;
    private final StudentService studentService;

    // Inject sẵn các phương thức thanh toán của bạn vào đây
    private final CashPayment cashPayment;
    private final WalletPayment walletPayment;
    private final BankTransferPayment bankTransferPayment;

    public CheckoutController(OrderService orderService, CartService cartService,
                              StudentService studentService, CashPayment cashPayment,
                              WalletPayment walletPayment, BankTransferPayment bankTransferPayment) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.studentService = studentService;
        this.cashPayment = cashPayment;
        this.walletPayment = walletPayment;
        this.bankTransferPayment = bankTransferPayment;
    }

    // API: Thanh toán giỏ hàng
    @PostMapping("/{studentId}")
    public Object processCheckout(@PathVariable String studentId, @RequestBody CheckoutRequest request) {
        try {
            Student student = studentService.findById(studentId);
            if (student == null) return "Lỗi: Không tìm thấy sinh viên!";

            Cart cart = cartService.getCartByStudentId(studentId);
            if (cart.isEmpty()) return "Lỗi: Giỏ hàng trống!";

            // 1. Tạo đơn hàng (Trạng thái PENDING)
            Order order = orderService.createOrder(student, cart);

            // 2. Chọn phương thức thanh toán dựa trên chữ Client gửi lên
            PaymentMethod method = selectPaymentMethod(request.getPaymentMethod());
            if (method == null) return "Lỗi: Phương thức thanh toán không hợp lệ!";

            // 3. Thực hiện thanh toán (Dùng code đa hình siêu xịn của bạn)
            boolean success = orderService.processPayment(order, student, method, cart);

            if (success) {
                return order; // Trả về thông tin hóa đơn nếu thành công
            } else {
                return "Thanh toán thất bại (Có thể do không đủ số dư ví).";
            }
        } catch (Exception e) {
            return "Lỗi hệ thống: " + e.getMessage();
        }
    }

    // Hàm phụ trợ: Chuyển chuỗi Text thành Class tương ứng
    private PaymentMethod selectPaymentMethod(String type) {
        if (type == null) return null;
        switch (type.toUpperCase()) {
            case "CASH": return cashPayment;
            case "WALLET": return walletPayment;
            case "BANK": return bankTransferPayment;
            default: return null;
        }
    }
}