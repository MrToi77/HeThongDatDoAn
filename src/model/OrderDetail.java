package model;

/**
 * Lớp OrderDetail lưu chi tiết từng món trong đơn hàng.
 * Snapshot tại thời điểm đặt (không bị ảnh hưởng nếu Food thay đổi sau đó).
 */
public class OrderDetail {

    private String foodId;      // Mã món
    private String foodName;    // Tên món (snapshot)
    private double unitPrice;   // Đơn giá tại thời điểm đặt
    private int quantity;       // Số lượng
    private double subtotal;    // Thành tiền

    public OrderDetail(String foodId, String foodName, double unitPrice, int quantity) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = unitPrice * quantity;
    }

    // ==================== Getters ====================

    public String getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Hiển thị chi tiết món trong đơn hàng.
     */
    public void displayInfo() {
        System.out.printf("  %-25s | SL: %3d | Đơn giá: %8.0f VND | Thành tiền: %10.0f VND%n",
                foodName, quantity, unitPrice, subtotal);
    }

    @Override
    public String toString() {
        return foodId + "," + foodName + "," + unitPrice + "," + quantity + "," + subtotal;
    }
}
