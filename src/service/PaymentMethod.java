package service;

import model.Order;
import model.Student;

/**
 * Interface PaymentMethod định nghĩa hợp đồng cho tất cả hình thức thanh toán.
 *
 * Thiết kế theo Open/Closed Principle: dễ dàng thêm phương thức thanh toán mới
 * (VD: MoMo, ZaloPay) chỉ bằng cách tạo class mới implements interface này,
 * không cần sửa code hiện có.
 */
public interface PaymentMethod {

    /**
     * Thực hiện thanh toán cho đơn hàng.
     *
     * @param order   Đơn hàng cần thanh toán
     * @param student Sinh viên thực hiện thanh toán
     * @return true nếu thanh toán thành công, false nếu thất bại
     */
    boolean pay(Order order, Student student);

    /**
     * Trả về tên hiển thị của hình thức thanh toán.
     *
     * @return Tên phương thức thanh toán
     */
    String getMethodName();
}
