package api;

import api.dto.CheckoutRequest;
import model.Cart;
import model.Order;
import model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.*;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private final OrderService orderService;
    private final CartService cartService;
    private final StudentService studentService;

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
    public ResponseEntity<?> processCheckout(@PathVariable String studentId,
                                             @RequestBody CheckoutRequest request) {
        try {
            Student student = studentService.findById(studentId);
            if (student == null) {
                return ResponseEntity.badRequest().body("Lỗi: Không tìm thấy sinh viên!");
            }

            Cart cart = cartService.getCartByStudentId(studentId);
            if (cart.isEmpty()) {
                return ResponseEntity.badRequest().body("Lỗi: Giỏ hàng trống!");
            }

            PaymentMethod method = selectPaymentMethod(request.getPaymentMethod());
            if (method == null) {
                return ResponseEntity.badRequest().body("Lỗi: Phương thức thanh toán không hợp lệ! Dùng: CASH, WALLET, BANK");
            }

            Order order = orderService.createOrder(student, cart);
            boolean success = orderService.processPayment(order, student, method, cart);

            if (success) {
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.badRequest().body("Thanh toán thất bại: Số dư ví không đủ.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    private PaymentMethod selectPaymentMethod(String type) {
        if (type == null) return null;
        switch (type.toUpperCase()) {
            case "CASH":   return cashPayment;
            case "WALLET": return walletPayment;
            case "BANK":   return bankTransferPayment;
            default:       return null;
        }
    }
}