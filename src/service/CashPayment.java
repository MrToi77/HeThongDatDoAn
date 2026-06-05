package service;

import model.Order;
import model.Student;

/**
 * Thanh toán bằng tiền mặt.
 * Không cần kiểm tra số dư tài khoản, xác nhận thanh toán ngay.
 */
public class CashPayment implements PaymentMethod {

    @Override
    public boolean pay(Order order, Student student) {
        // Thanh toán tiền mặt không trừ tài khoản, chỉ cập nhật trạng thái đơn
        order.setPaymentMethod(getMethodName());
        order.setStatus(model.Order.OrderStatus.PAID);

        System.out.println("Thanh toán bằng tiền mặt thành công!");
        System.out.printf("  Vui lòng chuẩn bị: %.0f VND khi nhận hàng.%n", order.getTotalAmount());
        return true;
    }

    @Override
    public String getMethodName() {
        return "Tiền mặt";
    }
}
