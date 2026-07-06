package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Order;
import model.OrderDetail;
import service.OrderService;
import utils.HttpUtil;

import java.io.IOException;
import java.util.List;

public class OrderHandler implements HttpHandler {

    private final OrderService orderService;

    public OrderHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path   = exchange.getRequestURI().getPath(); // VD: /api/orders hoặc /api/orders/student/SV001

        switch (method) {
            case "GET" -> handleGet(exchange, path);
            default    -> HttpUtil.sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }

    // GET /api/orders            → lấy tất cả đơn hàng
    // GET /api/orders?id=ORD-... → lấy 1 đơn theo mã
    // GET /api/orders?studentId=SV001 → lấy đơn theo sinh viên
    private void handleGet(HttpExchange exchange, String path) throws IOException {
        String query = exchange.getRequestURI().getQuery(); // VD: "id=ORD-123" hoặc "studentId=SV001"

        // Lấy đơn theo mã đơn hàng: GET /api/orders?id=ORD-20240101-0001
        if (query != null && query.startsWith("id=")) {
            String orderId = query.substring(3);
            Order order = orderService.findOrderById(orderId);
            if (order == null) {
                HttpUtil.sendResponse(exchange, 404, "{\"error\":\"Không tìm thấy đơn hàng: " + orderId + "\"}");
            } else {
                HttpUtil.sendResponse(exchange, 200, orderToJson(order));
            }
            return;
        }

        // Lấy đơn theo sinh viên: GET /api/orders?studentId=SV001
        if (query != null && query.startsWith("studentId=")) {
            String studentId = query.substring(10);
            List<Order> orders = orderService.getOrdersByStudentId(studentId);
            HttpUtil.sendResponse(exchange, 200, ordersToJson(orders));
            return;
        }

        // Lấy tất cả đơn hàng: GET /api/orders
        List<Order> orders = orderService.getAllOrders();
        HttpUtil.sendResponse(exchange, 200, ordersToJson(orders));
    }

    // Chuyển 1 Order thành JSON
    private String orderToJson(Order o) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(String.format("\"orderId\":\"%s\",", o.getOrderId()));
        sb.append(String.format("\"studentId\":\"%s\",", o.getStudentId()));
        sb.append(String.format("\"orderTime\":\"%s\",", o.getOrderTime()));
        sb.append(String.format("\"totalAmount\":%.0f,", o.getTotalAmount()));
        sb.append(String.format("\"status\":\"%s\",", o.getStatus()));
        sb.append(String.format("\"paymentMethod\":\"%s\",", o.getPaymentMethod()));
        sb.append("\"details\":");
        sb.append(detailsToJson(o.getDetails()));
        sb.append("}");
        return sb.toString();
    }

    // Chuyển danh sách Order thành JSON array
    private String ordersToJson(List<Order> orders) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < orders.size(); i++) {
            sb.append(orderToJson(orders.get(i)));
            if (i < orders.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    // Chuyển danh sách OrderDetail thành JSON array
    private String detailsToJson(List<OrderDetail> details) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < details.size(); i++) {
            OrderDetail d = details.get(i);
            sb.append(String.format(
                    "{\"foodId\":\"%s\",\"foodName\":\"%s\",\"unitPrice\":%.0f,\"quantity\":%d,\"subtotal\":%.0f}",
                    d.getFoodId(), d.getFoodName(), d.getUnitPrice(), d.getQuantity(), d.getSubtotal()
            ));
            if (i < details.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
