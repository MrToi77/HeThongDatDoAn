package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Student;
import service.StudentService;
import utils.HttpUtil;

import java.io.IOException;
import java.util.List;

public class StudentHandler implements HttpHandler {

    private final StudentService studentService;

    public StudentHandler(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path   = exchange.getRequestURI().getPath(); // /api/students hoặc /api/students/SV001

        switch (method) {
            case "GET"    -> handleGet(exchange);
            case "POST"   -> handlePost(exchange);
            case "PUT"    -> handlePut(exchange, path);
            case "DELETE" -> handleDelete(exchange, path);
            default       -> HttpUtil.sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }

    // GET /api/students → tất cả sinh viên
    private void handleGet(HttpExchange exchange) throws IOException {
        List<Student> students = studentService.getAllStudents();
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < students.size(); i++) {
            sb.append(studentToJson(students.get(i)));
            if (i < students.size() - 1) sb.append(",");
        }
        sb.append("]");
        HttpUtil.sendResponse(exchange, 200, sb.toString());
    }

    // POST /api/students → tạo sinh viên mới
    private void handlePost(HttpExchange exchange) throws IOException {
        String body = HttpUtil.readBody(exchange);
        try {
            String id          = parseField(body, "id");
            String fullName    = parseField(body, "fullName");
            String phone       = parseField(body, "phoneNumber");
            String roomAddress = parseField(body, "roomAddress");
            double balance     = Double.parseDouble(parseField(body, "accountBalance"));

            Student student = new Student(id, fullName, phone, roomAddress, balance);
            studentService.registerStudent(student);
            HttpUtil.sendResponse(exchange, 201, "{\"message\":\"Thêm sinh viên thành công\"}");
        } catch (IllegalArgumentException e) {
            HttpUtil.sendResponse(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Dữ liệu không hợp lệ: " + e.getMessage() + "\"}");
        }
    }

    // PUT /api/students/{id} → cập nhật sinh viên
    private void handlePut(HttpExchange exchange, String path) throws IOException {
        String id = extractId(path);
        String body = HttpUtil.readBody(exchange);
        try {
            Student student = studentService.findById(id);
            if (student == null) {
                HttpUtil.sendResponse(exchange, 404, "{\"error\":\"Không tìm thấy sinh viên: " + id + "\"}");
                return;
            }
            // Cập nhật các trường nếu có trong body
            trySet(body, "fullName",    v -> student.setFullName(v));
            trySet(body, "phoneNumber", v -> student.setPhoneNumber(v));
            trySet(body, "roomAddress", v -> student.setRoomAddress(v));
            trySetDouble(body, "accountBalance", v -> student.setAccountBalance(v));

            studentService.updateStudent(student);
            HttpUtil.sendResponse(exchange, 200, studentToJson(student));
        } catch (Exception e) {
            HttpUtil.sendResponse(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // DELETE /api/students/{id} → xóa sinh viên
    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String id = extractId(path);
        try {
            studentService.deleteStudent(id);
            HttpUtil.sendResponse(exchange, 200, "{\"message\":\"Đã xóa sinh viên " + id + "\"}");
        } catch (Exception e) {
            HttpUtil.sendResponse(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private String extractId(String path) {
        // /api/students/SV001 → SV001
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }

    private String studentToJson(Student s) {
        return String.format(
            "{\"id\":\"%s\",\"fullName\":\"%s\",\"phoneNumber\":\"%s\"," +
            "\"roomAddress\":\"%s\",\"accountBalance\":%.0f,\"activeTime\":\"%s\"}",
            s.getId(), s.getFullName(), s.getPhoneNumber(),
            s.getRoomAddress(), s.getAccountBalance(),
            s.getActiveTime() != null ? s.getActiveTime() : ""
        );
    }

    private void trySet(String json, String field, java.util.function.Consumer<String> setter) {
        try { setter.accept(parseField(json, field)); } catch (Exception ignored) {}
    }

    private void trySetDouble(String json, String field, java.util.function.Consumer<Double> setter) {
        try { setter.accept(Double.parseDouble(parseField(json, field))); } catch (Exception ignored) {}
    }

    private String parseField(String json, String field) {
        String key = "\"" + field + "\":";
        int start = json.indexOf(key) + key.length();
        if (start < key.length()) throw new IllegalArgumentException("Thiếu trường: " + field);
        if (json.charAt(start) == '"') { start++; return json.substring(start, json.indexOf('"', start)); }
        int end = json.indexOf(',', start); if (end == -1) end = json.indexOf('}', start);
        return json.substring(start, end).trim();
    }
}
