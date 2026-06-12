package service;

import model.Order;
import model.Student;
import org.springframework.stereotype.Component;

/**
 * Thanh toán bằng ví sinh viên (số dư tài khoản).
 * Tự động trừ tiền từ tài khoản nếu số dư đủ.
 */
@Component
public class WalletPayment implements PaymentMethod {

    @Override
    public boolean pay(Order order, Student student) {
        double amount = order.getTotalAmount();

        // Kiểm tra số dư
        if (student.getAccountBalance() < amount) {
            System.out.printf("Thanh toán thất bại! Số dư không đủ.%n");
            System.out.printf("  Số dư hiện tại : %.0f VND%n", student.getAccountBalance());
            System.out.printf("  Cần thanh toán : %.0f VND%n", amount);
            System.out.printf("  Còn thiếu      : %.0f VND%n", amount - student.getAccountBalance());
            return false;
        }

        // Trừ tiền
        student.deductBalance(amount);
        order.setPaymentMethod(getMethodName());
        order.setStatus(model.Order.OrderStatus.PAID);

        System.out.println("Thanh toán bằng ví sinh viên thành công!");
        System.out.printf("  Đã trừ        : %.0f VND%n", amount);
        System.out.printf("  Số dư còn lại : %.0f VND%n", student.getAccountBalance());
        return true;
    }

    @Override
    public String getMethodName() {
        return "Ví sinh viên";
    }
}
