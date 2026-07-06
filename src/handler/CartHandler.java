package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Cart;
import model.CartItem;
import model.Food;
import repository.FoodRepository;
import service.CartService;
import service.StudentService;
import utils.HttpUtil;

import java.io.IOException;
import java.util.List;

/**
 * Handler xử lý các request liên quan đến giỏ hàng.
 * GET    /api/carts/{studentId}         → xem giỏ hàng
 * POST   /api/carts/{studentId}/add     → thêm món vào giỏ
 * DELETE /api/carts/{studentId}/clear   → xóa giỏ hàng
 */
public class CartHandler implements HttpHandler {

    private final CartService cartService;
    private final FoodRepository foodRepository;

    public CartHandler(StudentService studentService, FoodRepository foodRepository, CartService cartService) {
        this.cartService = cartService;
        this.foodRepository = foodRepository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path   = exchange.getRequestURI().getPath(); // /api/carts/SV001 hoặc /api/carts/SV001/add

        switch (method) {
            case "GET"    -> handleGet(exchange, path);
            case "POST"   -> handlePost(exchange, path);
            case "DELETE" -> handleDelete(exchange, path);
            default       -> HttpUtil.sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }

    // GET /api/carts/{studentId}
    private void handleGet(HttpExchange exchange, String path) throws IOException {
        String studentId = extractStudentId(path); // lấy phần sau /api/carts/
        Cart cart = cartService.getCartByStudentId(studentId);
        HttpUtil.sendResponse(exchange, 200, cartToJson(cart));
    }

    // POST /api/carts/{studentId}/add
    private void handlePost(HttpExchange exchange, String path) throws IOException {
        String studentId = extractStudentId(path).replace("/add", "");
        String body = HttpUtil.readBody(exchange);
        try {
            String foodId  = parseField(body, "foodId");
            int quantity   = Integer.parseInt(parseField(body, "quantity"));

            Food food = foodRepository.findById(foodId);
            if (food == null) {
                HttpUtil.sendResponse(exchange, 404, "{\"error\":\"Không tìm thấy món: " + foodId + "\"}");
                return;
            }

            Cart cart = cartService.getCartByStudentId(studentId);
            cartService.addToCart(cart, food, quantity);
            HttpUtil.sendResponse(exchange, 200, cartToJson(cart));
        } catch (IllegalStateException | IllegalArgumentException e) {
            HttpUtil.sendResponse(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            HttpUtil.sendResponse(exchange, 400, "{\"error\":\"Dữ liệu không hợp lệ: " + e.getMessage() + "\"}");
        }
    }

    // DELETE /api/carts/{studentId}/clear
    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String studentId = extractStudentId(path).replace("/clear", "");
        Cart cart = cartService.getCartByStudentId(studentId);
        cartService.clearCart(cart);
        HttpUtil.sendResponse(exchange, 200, "{\"message\":\"Đã xóa giỏ hàng của " + studentId + "\"}");
    }

    // Lấy phần studentId (và sub-path) từ URL
    private String extractStudentId(String path) {
        // path = /api/carts/SV001 hoặc /api/carts/SV001/add
        return path.substring("/api/carts/".length());
    }

    // Chuyển Cart thành JSON
    private String cartToJson(Cart cart) {
        List<CartItem> items = cart.getItems();
        StringBuilder sb = new StringBuilder();
        sb.append("{\"studentId\":\"").append(cart.getStudentId()).append("\",\"items\":[");
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            Food f = item.getFood();
            sb.append(String.format(
                "{\"food\":{\"foodId\":\"%s\",\"name\":\"%s\",\"price\":%.0f}," +
                "\"quantity\":%d,\"subtotal\":%.0f}",
                f.getFoodId(), f.getName(), f.getPrice(),
                item.getQuantity(), item.getSubtotal()
            ));
            if (i < items.size() - 1) sb.append(",");
        }
        sb.append("],\"totalPrice\":").append(String.format("%.0f", cart.getTotalPrice())).append("}");
        return sb.toString();
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
