package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Cart đại diện cho giỏ hàng của sinh viên.
 * Quản lý danh sách CartItem.
 */
public class Cart {

    private String studentId;           // Mã sinh viên sở hữu giỏ hàng
    private List<CartItem> items;       // Danh sách các món trong giỏ

    public Cart(String studentId) {
        this.studentId = studentId;
        this.items = new ArrayList<>();
    }

    // ==================== Getters ====================

    public String getStudentId() {
        return studentId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    // ==================== Operations ====================

    /**
     * Thêm món vào giỏ hàng.
     * Nếu món đã tồn tại, cộng thêm số lượng.
     *
     * @param food     Món ăn cần thêm
     * @param quantity Số lượng
     */
    public void addItem(Food food, int quantity) {
        if (!food.isAvailable()) {
            throw new IllegalStateException("Món '" + food.getName() + "' đã hết hàng, không thể thêm vào giỏ!");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("Số lượng phải >= 1!");
        }

        // Kiểm tra nếu món đã có trong giỏ thì cộng thêm số lượng
        for (CartItem item : items) {
            if (item.getFood().getFoodId().equals(food.getFoodId())) {
                item.setQuantity(item.getQuantity() + quantity);
                System.out.println("Đã cập nhật số lượng: " + food.getName()
                        + " x" + item.getQuantity());
                return;
            }
        }

        items.add(new CartItem(food, quantity));
        System.out.println("Đã thêm vào giỏ: " + food.getName() + " x" + quantity);
    }

    /**
     * Xóa món khỏi giỏ hàng theo mã món.
     *
     * @param foodId Mã món cần xóa
     */
    public void removeItem(String foodId) {
        boolean removed = items.removeIf(item -> item.getFood().getFoodId().equals(foodId));
        if (!removed) {
            throw new IllegalArgumentException("Không tìm thấy món với mã: " + foodId + " trong giỏ hàng.");
        }
        System.out.println("Đã xóa món khỏi giỏ hàng.");
    }

    /**
     * Cập nhật số lượng của một món trong giỏ.
     *
     * @param foodId      Mã món
     * @param newQuantity Số lượng mới (>= 1)
     */
    public void updateQuantity(String foodId, int newQuantity) {
        for (CartItem item : items) {
            if (item.getFood().getFoodId().equals(foodId)) {
                item.setQuantity(newQuantity); // CartItem tự validate
                System.out.println("Đã cập nhật số lượng '"
                        + item.getFood().getName() + "' thành " + newQuantity);
                return;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy món với mã: " + foodId + " trong giỏ hàng.");
    }

    /**
     * Tính tổng tiền của toàn bộ giỏ hàng.
     *
     * @return Tổng tiền
     */
    public double getTotalPrice() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    /**
     * Kiểm tra giỏ hàng có rỗng không.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Xóa toàn bộ giỏ hàng (sau khi đặt hàng thành công).
     */
    public void clear() {
        items.clear();
    }

    /**
     * Hiển thị giỏ hàng ra console.
     */
    public void displayCart() {
        if (isEmpty()) {
            System.out.println("Giỏ hàng đang trống.");
            return;
        }
        System.out.println("===== Giỏ hàng =====");
        for (CartItem item : items) {
            item.displayInfo();
        }
        System.out.printf("Tổng cộng: %.0f VND%n", getTotalPrice());
        System.out.println("====================");
    }
}
