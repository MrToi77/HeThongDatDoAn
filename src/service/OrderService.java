package service;

import model.Cart;
import model.Order;
import model.Student;
import repository.OrderRepository;
import utils.OrderIdGenerator;

/**
 * Service xử lý logic đặt hàng và thanh toán.
 */
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Tạo đơn hàng mới từ giỏ hàng của sinh viên.
     * Business rules:
     *  - Giỏ hàng không được rỗng
     *
     * @param student Sinh viên đặt hàng
     * @param cart    Giỏ hàng
     * @return Đơn hàng mới (trạng thái PENDING)
     */
    public Order createOrder(Student student, Cart cart) {
        if (cart.isEmpty()) {
            throw new IllegalStateException("Không thể đặt hàng với giỏ hàng rỗng!");
        }

        String orderId = OrderIdGenerator.generate();
        Order order = new Order(orderId, student.getId(), cart.getItems());

        orderRepository.save(order);
        System.out.println("Đã tạo đơn hàng: " + orderId);
        return order;
    }

    /**
     * Thanh toán đơn hàng bằng phương thức được chỉ định.
     * Sau khi thanh toán thành công, giỏ hàng được xóa.
     *
     * @param order         Đơn hàng cần thanh toán
     * @param student       Sinh viên thanh toán
     * @param paymentMethod Phương thức thanh toán (polymorphism)
     * @param cart          Giỏ hàng (sẽ được xóa nếu thành công)
     * @return true nếu thanh toán thành công
     */
    public boolean processPayment(Order order, Student student,
                                  PaymentMethod paymentMethod, Cart cart) {
        if (order.getStatus() == Order.OrderStatus.PAID) {
            throw new IllegalStateException("Đơn hàng này đã được thanh toán!");
        }

        // Gọi phương thức thanh toán tương ứng (polymorphism)
        boolean success = paymentMethod.pay(order, student);

        if (success) {
            // Lưu lại đơn hàng đã cập nhật
            orderRepository.save(order);
            // Xóa giỏ hàng sau khi đặt thành công
            cart.clear();
            System.out.println("Đặt hàng và thanh toán thành công!");
            order.displayOrder();
        }

        return success;
    }

    /**
     * Xem lịch sử đơn hàng của một sinh viên.
     *
     * @param studentId Mã sinh viên
     */
    public void displayOrderHistory(String studentId) {
        var orders = orderRepository.findByStudentId(studentId);
        if (orders.isEmpty()) {
            System.out.println("Sinh viên " + studentId + " chưa có đơn hàng nào.");
            return;
        }
        System.out.println("===== Lịch sử đơn hàng của " + studentId + " =====");
        for (Order order : orders) {
            order.displayOrder();
        }
    }

    /**
     * Tìm đơn hàng theo mã đơn.
     *
     * @param orderId Mã đơn hàng
     * @return Order hoặc null
     */
    public Order findOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }
}
