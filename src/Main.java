import model.Cart;
import model.Food;
import model.Order;
import model.Student;
import repository.FoodRepository;
import repository.OrderRepository;
import repository.StudentRepository;
import service.*;

public class Main {

    public static void main( String[] args) {

        System.out.println("============================================");
        System.out.println("  HỆ THỐNG ĐẶT ĐỒ ĂN KÝ TÚC XÁ  ");
        System.out.println("============================================\n");

        // ── Khởi tạo Repository ──────────────────────────────────────
        FoodRepository foodRepo    = new FoodRepository();
        StudentRepository studentRepo = new StudentRepository();
        OrderRepository orderRepo   = new OrderRepository();

        // ── Khởi tạo Service ─────────────────────────────────────────
        FoodService foodService    = new FoodService(foodRepo);
        CartService cartService    = new CartService();
        StudentService studentService = new StudentService(studentRepo);
        OrderService orderService   = new OrderService(orderRepo);

        // ── 1. Xem danh sách món ăn ───────────────────────────────────
        System.out.println("\n>>> 1. DANH SÁCH MÓN ĂN:");
        foodService.displayAllFoods();

        // ── 2. Tìm kiếm món ──────────────────────────────────────────
        System.out.println("\n>>> 2. TÌM KIẾM MÓN 'cơm':");
        foodService.searchByName("cơm")
                .forEach(Food::displayInfo);

        // ── 3. Xem thông tin sinh viên ────────────────────────────────
        System.out.println("\n>>> 3. THÔNG TIN SINH VIÊN SV001:");
        Student student = studentService.findById("SV001");
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên!");
            return;
        }
        student.displayInfo();

        // ── 4. Demo giỏ hàng ─────────────────────────────────────────
        System.out.println("\n>>> 4. THAO TÁC GIỎ HÀNG:");
        Cart cart = new Cart(student.getId());

        // Thêm món vào giỏ
        Food f1 = foodService.findById("F001"); // Cơm sườn nướng - còn hàng
        Food f3 = foodService.findById("F003"); // Bún bò Huế - còn hàng
        Food f8 = foodService.findById("F008"); // Cơm chiên dương châu - hết hàng

        cartService.addToCart(cart, f1, 2);
        cartService.addToCart(cart, f3, 1);

        // Thử thêm món hết hàng -> phải ném exception
        System.out.println("\n--- Thử thêm món hết hàng ---");
        try {
            cartService.addToCart(cart, f8, 1);
        } catch (IllegalStateException e) {
            System.out.println("Lỗi (đúng như mong đợi): " + e.getMessage());
        }

        // Cập nhật số lượng
        System.out.println("\n--- Cập nhật số lượng F001 thành 3 ---");
        cartService.updateQuantity(cart, "F001", 3);

        // Xem giỏ hàng
        System.out.println("\n--- Xem giỏ hàng ---");
        cartService.viewCart(cart);

        // Xóa một món
        System.out.println("\n--- Xóa món F003 khỏi giỏ ---");
        cartService.removeFromCart(cart, "F003");
        cartService.viewCart(cart);

        // ── 5. Đặt hàng & Thanh toán bằng ví ────────────────────────
        System.out.println("\n>>> 5. ĐẶT HÀNG VÀ THANH TOÁN (Ví sinh viên):");
        demoWalletPayment(orderService, cart, student);

        // ── 6. Đặt hàng & Thanh toán tiền mặt ───────────────────────
        System.out.println("\n>>> 6. ĐẶT HÀNG VÀ THANH TOÁN (Tiền mặt):");
        demoCashPayment(foodService, cartService, orderService, cart, student);

        // ── 7. Demo thanh toán không đủ số dư ────────────────────────
        System.out.println("\n>>> 7. DEMO THANH TOÁN KHÔNG ĐỦ SỐ DƯ:");
        demoInsufficientBalance(foodService, cartService, orderService, cart, student);

        // ── 8. Lịch sử đơn hàng ──────────────────────────────────────
        System.out.println("\n>>> 8. LỊCH SỬ ĐƠN HÀNG CỦA SV001:");
        orderService.displayOrderHistory("SV001");

        System.out.println("\n============================================");
        System.out.println("  KẾT THÚC DEMO  ");
        System.out.println("============================================");
    }

    /** Demo thanh toán bằng ví - trường hợp thành công */
    private static void demoWalletPayment(OrderService orderService,
                                          Cart cart, Student student) {
        try {
            Order order = orderService.createOrder(student, cart);
            PaymentMethod walletPay = new WalletPayment();
            orderService.processPayment(order, student, walletPay, cart);
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    /** Demo thanh toán tiền mặt */
    private static void demoCashPayment(FoodService foodService, CartService cartService,
                                        OrderService orderService, Cart cart, Student student) {
        Food f6 = foodService.findById("F006"); // Trà sữa
        cartService.addToCart(cart, f6, 2);
        cartService.viewCart(cart);

        try {
            Order order = orderService.createOrder(student, cart);
            PaymentMethod cashPay = new CashPayment();
            orderService.processPayment(order, student, cashPay, cart);
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    /** Demo trường hợp không đủ số dư ví */
    private static void demoInsufficientBalance(FoodService foodService, CartService cartService,
                                                OrderService orderService, Cart cart, Student student) {
        // SV001 còn ít tiền -> thêm món đắt
        Food f1 = foodService.findById("F001");
        cartService.addToCart(cart, f1, 20); // 20 * 35000 = 700000 VND -> quá số dư
        cartService.viewCart(cart);

        try {
            Order order = orderService.createOrder(student, cart);
            PaymentMethod walletPay = new WalletPayment();
            boolean success = orderService.processPayment(order, student, walletPay, cart);
            if (!success) {
                System.out.println("-> Hệ thống từ chối thanh toán.");
            }
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }

        // Dọn giỏ cho gọn
        cart.clear();
    }
}