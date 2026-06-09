package service;

import model.Cart;
import model.Food;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Service xử lý logic nghiệp vụ liên quan đến giỏ hàng.
 */
@Service
public class CartService {
    private final Map<String, Cart> userCarts = new HashMap<>();
    /**key-value
    *STring là chìa khoá(vị trí)*
     * Cart là dữ liệu
     **/

    public Cart getCartByStudentId(String studentId) {
        // Bước 1: Kiểm tra xem trong tủ đã có giỏ hàng của sinh viên này chưa
        if (!userCarts.containsKey(studentId)) {

            // Nếu CHƯA CÓ: Tạo một giỏ hàng mới tinh
            Cart newCart = new Cart(studentId);

            // Sau đó cất giỏ hàng này vào tủ (Map)
            userCarts.put(studentId, newCart);
        }

        // Bước 2: Lấy giỏ hàng từ trong tủ ra và trả về
        return userCarts.get(studentId);
    }

    /**
     * Thêm món vào giỏ hàng.
     * Kiểm tra: món phải còn hàng, số lượng >= 1.
     *
     * @param cart     Giỏ hàng của sinh viên
     * @param food     Món ăn muốn thêm
     * @param quantity Số lượng
     */
    public void addToCart(Cart cart, Food food, int quantity) {
        if (food == null) {
            throw new IllegalArgumentException("Món ăn không tồn tại!");
        }
        if (!food.isAvailable()) {
            throw new IllegalStateException("Món '" + food.getName() + "' đã hết hàng!");
        }
        cart.addItem(food, quantity);
    }

    /**
     * Xóa món khỏi giỏ hàng theo mã món.
     *
     * @param cart   Giỏ hàng
     * @param foodId Mã món cần xóa
     */
    public void removeFromCart(Cart cart, String foodId) {
        cart.removeItem(foodId);
    }

    /**
     * Cập nhật số lượng một món trong giỏ.
     *
     * @param cart        Giỏ hàng
     * @param foodId      Mã món
     * @param newQuantity Số lượng mới (>= 1)
     */
    public void updateQuantity(Cart cart, String foodId, int newQuantity) {
        cart.updateQuantity(foodId, newQuantity);
    }

    /**
     * Hiển thị giỏ hàng.
     *
     * @param cart Giỏ hàng cần hiển thị
     */
    public void viewCart(Cart cart) {
        cart.displayCart();
    }

    /**
     * Xóa toàn bộ giỏ hàng.
     *
     * @param cart Giỏ hàng cần xóa
     */
    public void clearCart(Cart cart) {
        cart.clear();
        System.out.println("Đã xóa toàn bộ giỏ hàng.");
    }

}
