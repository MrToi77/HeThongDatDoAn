package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Admin;
import model.Student;
import repository.AdminRepository;
import repository.StudentRepository;
import utils.HttpUtil;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handler xử lý đăng nhập và đăng ký.
 *
 * POST /api/auth/login    → đăng nhập (student hoặc admin)
 * POST /api/auth/register → đăng ký (student hoặc admin)
 */
public class AuthHandler implements HttpHandler {

    private final StudentRepository studentRepo;
    private final AdminRepository   adminRepo;
    private final AtomicInteger     studentIdCounter = new AtomicInteger(100);

    public AuthHandler(StudentRepository studentRepo, AdminRepository adminRepo) {
        this.studentRepo = studentRepo;
        this.adminRepo   = adminRepo;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            HttpUtil.sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }

        String path = exchange.getRequestURI().getPath();
        String body = HttpUtil.readBody(exchange);

        if (path.endsWith("/login")) {
            handleLogin(exchange, body);
        } else if (path.endsWith("/register")) {
            handleRegister(exchange, body);
        } else {
            HttpUtil.sendResponse(exchange, 404, "{\"error\":\"Not found\"}");
        }
    }

    // POST /api/auth/login
    // Body: {"role":"student","phone":"0901234567","password":"123456"}
    //    or {"role":"admin","phone":"0000000000","password":"admin123"}
    private void handleLogin(HttpExchange exchange, String body) throws IOException {
        try {
            String role     = parseField(body, "role");
            String phone    = parseField(body, "phone");
            String password = parseField(body, "password");

            if ("admin".equalsIgnoreCase(role)) {
                Admin admin = adminRepo.findByPhone(phone);
                if (admin == null || !admin.getPassword().equals(password)) {
                    HttpUtil.sendResponse(exchange, 401, "{\"error\":\"Sai số điện thoại hoặc mật khẩu!\"}");
                    return;
                }
                HttpUtil.sendResponse(exchange, 200, String.format(
                    "{\"role\":\"admin\",\"id\":\"%s\",\"phone\":\"%s\"}",
                    admin.getId(), admin.getPhoneNumber()));

            } else if ("student".equalsIgnoreCase(role)) {
                Student student = studentRepo.findByPhone(phone);
                if (student == null || !student.getPassword().equals(password)) {
                    HttpUtil.sendResponse(exchange, 401, "{\"error\":\"Sai số điện thoại hoặc mật khẩu!\"}");
                    return;
                }
                student.updateActiveTime();
                studentRepo.save(student);
                HttpUtil.sendResponse(exchange, 200, String.format(
                    "{\"role\":\"student\",\"id\":\"%s\",\"fullName\":\"%s\"," +
                    "\"phone\":\"%s\",\"roomAddress\":\"%s\",\"accountBalance\":%.0f}",
                    student.getId(), student.getFullName(), student.getPhoneNumber(),
                    student.getRoomAddress(), student.getAccountBalance()));
            } else {
                HttpUtil.sendResponse(exchange, 400, "{\"error\":\"role phải là 'student' hoặc 'admin'\"}");
            }
        } catch (Exception e) {
            HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Dữ liệu không hợp lệ: " + e.getMessage() + "\"}");
        }
    }

    // POST /api/auth/register
    // Sinh viên: {"role":"student","studentId":"SV004","fullName":"...","phone":"...","room":"...","balance":100000,"password":"..."}
    // Admin:     {"role":"admin","phone":"...","password":"..."}
    private void handleRegister(HttpExchange exchange, String body) throws IOException {
        try {
            String role = parseField(body, "role");

            if ("admin".equalsIgnoreCase(role)) {
                String phone    = parseField(body, "phone");
                String password = parseField(body, "password");

                if (adminRepo.findByPhone(phone) != null) {
                    HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Số điện thoại này đã được đăng ký!\"}");
                    return;
                }
                String adminId = "ADMIN" + String.format("%03d", studentIdCounter.getAndIncrement());
                Admin admin = new Admin(adminId, phone, password);
                adminRepo.save(admin);
                HttpUtil.sendResponse(exchange, 201, String.format(
                    "{\"message\":\"Đăng ký admin thành công!\",\"id\":\"%s\"}", adminId));

            } else if ("student".equalsIgnoreCase(role)) {
                String studentId = parseField(body, "studentId");
                String fullName  = parseField(body, "fullName");
                String phone     = parseField(body, "phone");
                String room      = parseField(body, "room");
                String password  = parseField(body, "password");
                double balance   = Double.parseDouble(parseField(body, "balance"));

                if (studentRepo.findById(studentId) != null) {
                    HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Mã sinh viên đã tồn tại!\"}");
                    return;
                }
                if (studentRepo.findByPhone(phone) != null) {
                    HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Số điện thoại này đã được đăng ký!\"}");
                    return;
                }
                Student student = new Student(studentId, fullName, phone, room, balance, password);
                studentRepo.save(student);
                HttpUtil.sendResponse(exchange, 201, String.format(
                    "{\"message\":\"Đăng ký thành công!\",\"id\":\"%s\"}", studentId));
            } else {
                HttpUtil.sendResponse(exchange, 400, "{\"error\":\"role phải là 'student' hoặc 'admin'\"}");
            }
        } catch (Exception e) {
            HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Dữ liệu không hợp lệ: " + e.getMessage() + "\"}");
        }
    }

    private String parseField(String json, String field) {
        String key = "\"" + field + "\":";
        int start = json.indexOf(key) + key.length();
        if (start < key.length()) throw new IllegalArgumentException("Thiếu trường: " + field);
        if (json.charAt(start) == '"') {
            start++;
            return json.substring(start, json.indexOf('"', start));
        }
        int end = json.indexOf(',', start);
        if (end == -1) end = json.indexOf('}', start);
        return json.substring(start, end).trim();
    }
}
