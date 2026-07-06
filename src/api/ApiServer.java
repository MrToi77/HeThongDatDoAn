package api;

import com.sun.net.httpserver.HttpServer;
import handler.*;
import repository.*;
import service.OrderService;
import service.StudentService;
import service.CartService;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ApiServer {

    private static final int PORT = 8080;

    public static void start() throws IOException {
        // Repositories
        FoodRepository    foodRepo    = new FoodRepository();
        StudentRepository studentRepo = new StudentRepository();
        OrderRepository   orderRepo   = new OrderRepository();
        AdminRepository   adminRepo   = new AdminRepository();

        // Services
        OrderService   orderService   = new OrderService(orderRepo);
        StudentService studentService = new StudentService(studentRepo);
        CartService    cartService    = new CartService(); // 1 instance duy nhất dùng chung

        // HTML
        String html = loadHtml();

        // Server
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Giao diện web
        server.createContext("/", new StaticHandler(html));

        // Auth
        server.createContext("/api/auth", new AuthHandler(studentRepo, adminRepo));

        // API
        server.createContext("/api/foods",    new FoodHandler(foodRepo));
        server.createContext("/api/orders",   new OrderHandler(orderService));
        server.createContext("/api/students", new StudentHandler(studentService));
        server.createContext("/api/carts",    new CartHandler(studentService, foodRepo, cartService));
        server.createContext("/api/checkout", new CheckoutHandler(orderService, studentService, foodRepo, cartService));

        server.setExecutor(null);
        server.start();

        System.out.println("===========================================");
        System.out.println("  Server: http://localhost:" + PORT);
        System.out.println("  Admin mặc định: SĐT=0000000000 / mật khẩu=admin123");
        System.out.println("===========================================");
    }

    private static String loadHtml() throws IOException {
        InputStream is = ApiServer.class.getClassLoader().getResourceAsStream("index.html");
        if (is != null) return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        java.io.File file = new java.io.File("web/index.html");
        if (file.exists()) return new String(java.nio.file.Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        return "<h1>Không tìm thấy file index.html</h1>";
    }
}
