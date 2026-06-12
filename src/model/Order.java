package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Order đại diện cho một đơn hàng trong hệ thống.
 */
public class Order {

    /** Các trạng thái có thể của đơn hàng */
    public enum OrderStatus {
        PENDING,        // Chờ thanh toán
        PAID,           // Đã thanh toán
        CANCELLED       // Đã hủy
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private String orderId;                 // Mã đơn hàng (tự sinh)
    private String studentId;              // Mã sinh viên đặt
    private List<OrderDetail> details;     // Chi tiết từng món
    private double totalAmount;            // Tổng tiền
    private LocalDateTime orderTime;       // Thời gian đặt
    private OrderStatus status;            // Trạng thái đơn hàng
    private String paymentMethod;          // Hình thức thanh toán

    public Order(String orderId, String studentId, List<CartItem> cartItems) {
        this.orderId = orderId;
        this.studentId = studentId;
        this.orderTime = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
        this.details = new ArrayList<>();
        this.paymentMethod = "";

        // Chuyển CartItem -> OrderDetail (snapshot giá)
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail(
                    item.getFood().getFoodId(),
                    item.getFood().getName(),
                    item.getFood().getPrice(),
                    item.getQuantity()
            );
            this.details.add(detail);
        }

        // Tính tổng tiền
        this.totalAmount = details.stream().mapToDouble(OrderDetail::getSubtotal).sum();
    }

    // ==================== Getters & Setters ====================

    public String getOrderId() {
        return orderId;
    }

    public String getStudentId() {
        return studentId;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Hiển thị thông tin đơn hàng chi tiết.
     */
    public void displayOrder() {
        System.out.println("========================================");
        System.out.println("       HÓA ĐƠN ĐẶT MÓN                ");
        System.out.println("========================================");
        System.out.println("Mã đơn hàng   : " + orderId);
        System.out.println("Mã sinh viên  : " + studentId);
        System.out.println("Thời gian đặt : " + orderTime.format(FORMATTER));
        System.out.println("Trạng thái    : " + getStatusDisplay());
        System.out.println("Thanh toán    : " + (paymentMethod.isEmpty() ? "Chưa thanh toán" : paymentMethod));
        System.out.println("----------------------------------------");
        System.out.println("Chi tiết đơn hàng:");
        for (OrderDetail detail : details) {
            detail.displayInfo();
        }
        System.out.println("----------------------------------------");
        System.out.printf("Tổng cộng     : %.0f VND%n", totalAmount);
        System.out.println("========================================");
    }

    /** Trả về trạng thái đơn hàng dưới dạng tiếng Việt */
    private String getStatusDisplay() {
        switch (status) {
            case PENDING:   return "Chờ thanh toán";
            case PAID:      return "Đã thanh toán";
            case CANCELLED: return "Đã hủy";
            default:        return "Không xác định";
        }
    }

    @Override
    public String toString() {
        return orderId + "," + studentId + "," + orderTime.format(FORMATTER)
                + "," + totalAmount + "," + status + "," + paymentMethod;
    }
}
