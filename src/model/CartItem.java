package model;

/**
 * Lớp CartItem đại diện cho một dòng món ăn trong giỏ hàng.
 * Tách riêng khỏi Food để lưu thêm số lượng và thành tiền theo giỏ hàng.
 */
public class CartItem {

    private Food food;       // Tham chiếu đến món ăn
    private int quantity;    // Số lượng đặt

    public CartItem(Food food, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Số lượng món không được nhỏ hơn 1!");
        }
        this.food = food;
        this.quantity = quantity;
    }

    // ==================== Getters & Setters ====================

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Cập nhật số lượng món trong giỏ.
     *
     * @param quantity Số lượng mới (phải >= 1)
     */
    public void setQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Số lượng món không được nhỏ hơn 1!");
        }
        this.quantity = quantity;
    }

    /**
     * Tính thành tiền = giá món * số lượng.
     *
     * @return Thành tiền
     */
    public double getSubtotal() {
        return food.getPrice() * quantity;
    }

    /**
     * Hiển thị thông tin CartItem ra console.
     */
    public void displayInfo() {
        System.out.printf("  %-25s | SL: %3d | Đơn giá: %8.0f VND | Thành tiền: %10.0f VND%n",
                food.getName(), quantity, food.getPrice(), getSubtotal());
    }

    @Override
    public String toString() {
        return "CartItem{food=" + food.getName() + ", qty=" + quantity
                + ", subtotal=" + getSubtotal() + "}";
    }
}
