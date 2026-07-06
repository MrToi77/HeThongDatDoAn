package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Cart;
import model.Order;
import model.Student;
import repository.FoodRepository;
import repository.OrderRepository;
import service.*;
import utils.HttpUtil;

import java.io.IOException;

/**
 * POST /api/checkout/{studentId}  → tạo đơn mới từ giỏ hàng (trạng thái PENDING)
 * POST /api/checkout/pay          → thanh toán đơn đã có (PENDING → PAID)
 */
public class CheckoutHandler implements HttpHandler {

    private final OrderService        orderService;
    private final StudentService      studentService;
    private final CartService         cartService;
    private final CashPayment         cashPayment;
    private final WalletPayment       walletPayment;
    private final BankTransferPayment bankPayment;

    public CheckoutHandler(OrderService orderService, StudentService studentService,
                           FoodRepository foodRepository, CartService cartService) {
        this.orderService   = orderService;
        this.studentService = studentService;
        this.cartService    = cartService;
        this.cashPayment    = new CashPayment();
        this.walletPayment  = new WalletPayment();
        this.bankPayment    = new BankTransferPayment();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            HttpUtil.sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }

        String path = exchange.getRequestURI().getPath();
        String body = HttpUtil.readBody(exchange);

        // POST /api/checkout/pay → thanh toán đơn đã tạo sẵn
        if (path.endsWith("/pay")) {
            handlePayExisting(exchange, body);
            return;
        }

        // POST /api/checkout/{studentId} → tạo đơn mới từ giỏ hàng
        String studentId = path.substring("/api/checkout/".length());
        handleCreateAndCheckout(exchange, body, studentId);
    }

    /**
     * Tạo đơn mới từ giỏ hàng. Nếu paymentMethod là COD thì để PENDING (chưa thanh toán).
     */
    private void handleCreateAndCheckout(HttpExchange exchange, String body, String studentId) throws IOException {
        try {
            String methodStr = parseField(body, "paymentMethod");

            Student student = studentService.findById(studentId);
            if (student == null) {
                HttpUtil.sendResponse(exchange, 404, "{\"error\":\"Không tìm thấy sinh viên: " + studentId + "\"}");
                return;
            }

            Cart cart = cartService.getCartByStudentId(studentId);
            if (cart.isEmpty()) {
                HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Giỏ hàng trống!\"}");
                return;
            }

            // COD = tạo đơn PENDING, chưa thanh toán, xóa giỏ hàng
            if ("COD".equalsIgnoreCase(methodStr)) {
                Order order = orderService.createOrder(student, cart);
                order.setPaymentMethod("Chưa thanh toán");
                cart.clear();
                HttpUtil.sendResponse(exchange, 200, orderToJson(order));
                return;
            }

            PaymentMethod method = selectMethod(methodStr);
            if (method == null) {
                HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Phương thức không hợp lệ! Dùng: COD, WALLET, BANK, CASH\"}");
                return;
            }

            Order order = orderService.createOrder(student, cart);
            boolean success = orderService.processPayment(order, student, method, cart);
            if (success) HttpUtil.sendResponse(exchange, 200, orderToJson(order));
            else HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Thanh toán thất bại: Số dư không đủ!\"}");

        } catch (Exception e) {
            HttpUtil.sendResponse(exchange, 500, "{\"error\":\"Lỗi hệ thống: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Thanh toán đơn hàng đã có (PENDING).
     * Body: {"orderId":"ORD-...", "paymentMethod":"WALLET", "studentId":"SV001"}
     */
    private void handlePayExisting(HttpExchange exchange, String body) throws IOException {
        try {
            String orderId   = parseField(body, "orderId");
            String methodStr = parseField(body, "paymentMethod");
            String studentId = parseField(body, "studentId");

            Order order = orderService.findOrderById(orderId);
            if (order == null) {
                HttpUtil.sendResponse(exchange, 404, "{\"error\":\"Không tìm thấy đơn hàng: " + orderId + "\"}");
                return;
            }
            if (order.getStatus() == Order.OrderStatus.PAID) {
                HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Đơn hàng này đã được thanh toán!\"}");
                return;
            }

            Student student = studentService.findById(studentId);
            if (student == null) {
                HttpUtil.sendResponse(exchange, 404, "{\"error\":\"Không tìm thấy sinh viên!\"}");
                return;
            }

            // COD = thanh toán khi nhận hàng (đánh dấu PAID, không trừ ví)
            if ("COD".equalsIgnoreCase(methodStr)) {
                order.setPaymentMethod("Thanh toán khi nhận hàng");
                order.setStatus(Order.OrderStatus.PAID);
                orderService.saveOrder(order);
                HttpUtil.sendResponse(exchange, 200, orderToJson(order));
                return;
            }

            PaymentMethod method = selectMethod(methodStr);
            if (method == null) {
                HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Phương thức không hợp lệ!\"}");
                return;
            }

            // Dùng Cart rỗng vì đơn đã có sẵn — processPayment chỉ cần gọi pay()
            boolean success = method.pay(order, student);
            if (success) {
                orderService.saveOrder(order);
                studentService.updateStudent(student);
                HttpUtil.sendResponse(exchange, 200, orderToJson(order));
            } else {
                HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Thanh toán thất bại: Số dư không đủ!\"}");
            }

        } catch (Exception e) {
            HttpUtil.sendResponse(exchange, 500, "{\"error\":\"Lỗi hệ thống: " + e.getMessage() + "\"}");
        }
    }

    private PaymentMethod selectMethod(String type) {
        if (type == null) return null;
        return switch (type.toUpperCase()) {
            case "CASH"   -> cashPayment;
            case "WALLET" -> walletPayment;
            case "BANK"   -> bankPayment;
            default       -> null;
        };
    }

    private String orderToJson(Order o) {
        return String.format(
            "{\"orderId\":\"%s\",\"studentId\":\"%s\",\"totalAmount\":%.0f,\"status\":\"%s\",\"paymentMethod\":\"%s\"}",
            o.getOrderId(), o.getStudentId(), o.getTotalAmount(), o.getStatus(), o.getPaymentMethod()
        );
    }

    private String parseField(String json, String field) {
        String key = "\"" + field + "\":";
        int start = json.indexOf(key) + key.length();
        if (start < key.length()) throw new IllegalArgumentException("Thiếu trường: " + field);
        if (json.charAt(start) == '"') { start++; return json.substring(start, json.indexOf('"', start)); }
        int end = json.indexOf(',', start); if (end == -1) end = json.indexOf('}', start);
        return json.substring(start, end).trim();
    }
}
