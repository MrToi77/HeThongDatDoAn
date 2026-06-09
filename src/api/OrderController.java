package api;

import model.Order;
import org.springframework.web.bind.annotation.*;
import repository.OrderRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // API: Lấy tất cả đơn hàng trong hệ thống
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // API: Xem chi tiết 1 đơn hàng theo mã Order (VD: ORD123)
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable String id) {
        return orderRepository.findById(id);
    }

    // API: Lấy danh sách các đơn hàng của riêng một sinh viên
    // Đường dẫn: GET http://localhost:8080/api/v1/orders/student/SV001
    @GetMapping("/student/{studentId}")
    public List<Order> getOrdersByStudent(@PathVariable String studentId) {
        return orderRepository.findByStudentId(studentId);
    }
}