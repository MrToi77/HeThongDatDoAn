package model;

import org.springframework.stereotype.Repository;

/**
 * Lớp Food đại diện cho một món ăn trong hệ thống.
 */
public class Food {

    private String foodId;       // Mã món ăn
    private String name;         // Tên món
    private double price;        // Giá tiền
    private String category;     // Loại món (Cơm, Bún, Đồ uống, ...)
    private boolean available;   // Trạng thái: true = còn hàng, false = hết hàng

    public Food(String foodId, String name, double price, String category, boolean available) {
        if (price < 0) {
            throw new IllegalArgumentException("Giá tiền không được âm!");
        }
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    // ==================== Getters & Setters ====================

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Giá tiền không được âm!");
        }
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Hiển thị thông tin món ăn ra console.
     */
    public void displayInfo() {
        String status = available ? "Còn hàng" : "Hết hàng";
        System.out.printf("%-8s | %-25s | %10.0f VND | %-15s | %s%n",
                foodId, name, price, category, status);
    }

    @Override
    public String toString() {
        return foodId + "," + name + "," + price + "," + category + "," + available;
    }
}
