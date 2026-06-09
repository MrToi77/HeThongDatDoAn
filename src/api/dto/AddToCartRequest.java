package api.dto;
//Hứng dữ liệu...
public class AddToCartRequest {
    private String foodId;
    private int quantity;

    // Getters và Setters
    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

