package service;

import model.Order;
import model.Student;

/**
 * Thanh toán bằng chuyển khoản ngân hàng.
 * Hiển thị thông tin tài khoản và xác nhận chuyển khoản.
 */
public class BankTransferPayment implements PaymentMethod {

    // Thông tin tài khoản ngân hàng nhận chuyển khoản (có thể cấu hình)
    private static final String BANK_NAME    = "Vietcombank";
    private static final String ACCOUNT_NO   = "1234567890";
    private static final String ACCOUNT_NAME = "KTX Trường Đại Học";

    @Override
    public boolean pay(Order order, Student student) {
        order.setPaymentMethod(getMethodName());
        order.setStatus(model.Order.OrderStatus.PAID);

        System.out.println("Thanh toán bằng chuyển khoản ngân hàng thành công!");
        System.out.println("  Thông tin chuyển khoản:");
        System.out.println("    Ngân hàng  : " + BANK_NAME);
        System.out.println("    Số TK      : " + ACCOUNT_NO);
        System.out.println("    Chủ TK     : " + ACCOUNT_NAME);
        System.out.printf("    Số tiền    : %.0f VND%n", order.getTotalAmount());
        System.out.println("    Nội dung   : " + order.getOrderId() + " " + student.getId());
        return true;
    }

    @Override
    public String getMethodName() {
        return "Chuyển khoản ngân hàng";
    }
}
