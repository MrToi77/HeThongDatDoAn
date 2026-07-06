package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Food;
import repository.FoodRepository;
import utils.HttpUtil;

import java.io.IOException;
import java.util.List;

public class FoodHandler implements HttpHandler {

    private final FoodRepository foodRepository;

    public FoodHandler(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET"    -> handleGet(exchange);
            case "POST"   -> handlePost(exchange);
            case "DELETE" -> handleDelete(exchange);
            default       -> HttpUtil.sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }

    // GET /api/foods          → lấy tất cả món ăn
    // GET /api/foods?id=F001  → lấy 1 món theo mã
    private void handleGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        // Lấy 1 món theo mã: GET /api/foods?id=F001
        if (query != null && query.startsWith("id=")) {
            String foodId = query.substring(3);
            Food food = foodRepository.findById(foodId);
            if (food == null) {
                HttpUtil.sendResponse(exchange, 404, "{\"error\":\"Không tìm thấy món ăn: " + foodId + "\"}");
            } else {
                HttpUtil.sendResponse(exchange, 200, foodToJson(food));
            }
            return;
        }

        // Lấy tất cả: GET /api/foods
        List<Food> foods = foodRepository.findAll();
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < foods.size(); i++) {
            sb.append(foodToJson(foods.get(i)));
            if (i < foods.size() - 1) sb.append(",");
        }
        sb.append("]");
        HttpUtil.sendResponse(exchange, 200, sb.toString());
    }

    // POST /api/foods
    // body: {"id":"F010","name":"Bún bò","price":25000,"category":"Bún","available":true}
    private void handlePost(HttpExchange exchange) throws IOException {
        String body = HttpUtil.readBody(exchange);
        try {
            String id        = parseField(body, "id");
            String name      = parseField(body, "name");
            double price     = Double.parseDouble(parseField(body, "price"));
            String category  = parseField(body, "category");
            boolean available = Boolean.parseBoolean(parseField(body, "available"));

            Food food = new Food(id, name, price, category, available);
            foodRepository.save(food);

            HttpUtil.sendResponse(exchange, 201, "{\"message\":\"Thêm món ăn thành công\"}");
        } catch (IllegalArgumentException e) {
            HttpUtil.sendResponse(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Dữ liệu không hợp lệ: " + e.getMessage() + "\"}");
        }
    }

    // DELETE /api/foods?id=F001
    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null || !query.startsWith("id=")) {
            HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Thiếu tham số id\"}");
            return;
        }
        String id = query.substring(3);
        Food food = foodRepository.findById(id);
        if (food == null) {
            HttpUtil.sendResponse(exchange, 404, "{\"error\":\"Không tìm thấy món ăn: " + id + "\"}");
            return;
        }
        foodRepository.deleteById(id);
        HttpUtil.sendResponse(exchange, 200, "{\"message\":\"Đã xóa món ăn " + id + "\"}");
    }

    // Chuyển 1 Food thành JSON
    private String foodToJson(Food f) {
        return String.format(
                "{\"foodId\":\"%s\",\"name\":\"%s\",\"price\":%.0f,\"category\":\"%s\",\"available\":%b}",
                f.getFoodId(), f.getName(), f.getPrice(), f.getCategory(), f.isAvailable()
        );
    }

    // Parse field từ JSON string thủ công, ví dụ: {"name":"Phở"} → "Phở"
    private String parseField(String json, String field) {
        String key = "\"" + field + "\":";
        int start = json.indexOf(key) + key.length();
        if (start < key.length()) {
            throw new IllegalArgumentException("Thiếu trường: " + field);
        }
        // Nếu value là string (có dấu ")
        if (json.charAt(start) == '"') {
            start++;
            int end = json.indexOf('"', start);
            return json.substring(start, end);
        }
        // Nếu value là số hoặc boolean
        int end = json.indexOf(',', start);
        if (end == -1) end = json.indexOf('}', start);
        return json.substring(start, end).trim();
    }
}
