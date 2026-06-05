package repository;

import model.Order;
import model.OrderDetail;
import utils.FileHelper;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository quản lý dữ liệu đơn hàng.
 * Lưu thông tin đơn hàng vào data/orders.csv
 * và chi tiết đơn vào data/order_details.csv
 */
public class OrderRepository implements Repository<Order, String> {

    private static final String ORDER_FILE        = "data/orders.csv";
    private static final String ORDER_DETAIL_FILE = "data/order_details.csv";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final Map<String, Order> cache = new LinkedHashMap<>();
    private boolean loaded = false;

    private void ensureLoaded() {
        if (!loaded) {
            loadFromFile();
            loaded = true;
        }
    }

    @Override
    public void save(Order order) {
        ensureLoaded();
        cache.put(order.getOrderId(), order);
        saveToFile();
    }

    @Override
    public Order findById(String orderId) {
        ensureLoaded();
        return cache.get(orderId);
    }

    @Override
    public List<Order> findAll() {
        ensureLoaded();
        return new ArrayList<>(cache.values());
    }

    @Override
    public void deleteById(String orderId) {
        ensureLoaded();
        cache.remove(orderId);
        saveToFile();
    }

    /**
     * Lấy tất cả đơn hàng của một sinh viên.
     *
     * @param studentId Mã sinh viên
     * @return Danh sách đơn hàng
     */
    public List<Order> findByStudentId(String studentId) {
        ensureLoaded();
        return cache.values().stream()
                .filter(o -> o.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    // ==================== File IO ====================

    private void loadFromFile() {
        // Load chi tiết đơn hàng trước
        Map<String, List<OrderDetail>> detailsMap = loadOrderDetails();

        File file = new File(ORDER_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;

                try {
                    String orderId   = parts[0].trim();
                    String studentId = parts[1].trim();
                    LocalDateTime time = LocalDateTime.parse(parts[2].trim(), FORMATTER);
                    double total     = Double.parseDouble(parts[3].trim());
                    Order.OrderStatus status = Order.OrderStatus.valueOf(parts[4].trim());
                    String payMethod = parts[5].trim();

                    // Tái tạo Order từ file (không có cart nên dùng constructor rỗng thông qua reflection workaround)
                    Order order = new Order(orderId, studentId, new ArrayList<>());
                    order.setStatus(status);
                    order.setPaymentMethod(payMethod);

                    // Gắn details từ file detail
                    List<OrderDetail> details = detailsMap.getOrDefault(orderId, new ArrayList<>());
                    order.getDetails().addAll(details);

                    cache.put(orderId, order);
                } catch (Exception e) {
                    System.err.println("Lỗi đọc đơn hàng: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc file orders.csv: " + e.getMessage());
        }
    }

    private Map<String, List<OrderDetail>> loadOrderDetails() {
        Map<String, List<OrderDetail>> map = new HashMap<>();
        File file = new File(ORDER_DETAIL_FILE);
        if (!file.exists()) return map;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;

                try {
                    String orderId  = parts[0].trim();
                    String foodId   = parts[1].trim();
                    String foodName = parts[2].trim();
                    double price    = Double.parseDouble(parts[3].trim());
                    int qty         = Integer.parseInt(parts[4].trim());

                    OrderDetail detail = new OrderDetail(foodId, foodName, price, qty);
                    map.computeIfAbsent(orderId, k -> new ArrayList<>()).add(detail);
                } catch (Exception e) {
                    System.err.println("Lỗi đọc chi tiết đơn: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc file order_details.csv: " + e.getMessage());
        }
        return map;
    }

    private void saveToFile() {
        FileHelper.ensureDirectoryExists(ORDER_FILE);
        FileHelper.ensureDirectoryExists(ORDER_DETAIL_FILE);

        // Ghi orders.csv
        try (PrintWriter writer = new PrintWriter(new FileWriter(ORDER_FILE))) {
            for (Order order : cache.values()) {
                writer.println(order.toString());
            }
        } catch (IOException e) {
            System.err.println("Lỗi ghi file orders.csv: " + e.getMessage());
        }

        // Ghi order_details.csv
        try (PrintWriter writer = new PrintWriter(new FileWriter(ORDER_DETAIL_FILE))) {
            for (Order order : cache.values()) {
                for (OrderDetail detail : order.getDetails()) {
                    writer.println(order.getOrderId() + "," + detail.toString());
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi ghi file order_details.csv: " + e.getMessage());
        }
    }
}
