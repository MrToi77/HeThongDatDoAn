package service;

import model.Cart;
import model.CartItem;
import model.Food;

public class CartService {

    public void addFood(Cart cart, Food food, int quantity)
            throws Exception {

        if (!food.isAvailable()) {
            throw new Exception("Mon da het hang!");
        }

        if (quantity < 1) {
            throw new Exception("So luong phai lon hon 0!");
        }

        for (CartItem item : cart.getItems()) {

            if (item.getFood().getFoodId()
                    .equals(food.getFoodId())) {

                item.setQuantity(
                        item.getQuantity() + quantity);

                return;
            }
        }

        cart.getItems().add(
                new CartItem(food, quantity));
    }

    public void removeFood(Cart cart, String foodId) {

        cart.getItems().removeIf(
                item -> item.getFood()
                        .getFoodId()
                        .equals(foodId));
    }

    public void updateQuantity(
            Cart cart,
            String foodId,
            int newQuantity)
            throws Exception {

        if (newQuantity < 1) {
            throw new Exception(
                    "So luong phai >= 1");
        }

        for (CartItem item : cart.getItems()) {

            if (item.getFood()
                    .getFoodId()
                    .equals(foodId)) {

                item.setQuantity(newQuantity);
                return;
            }
        }
    }

    public double getTotal(Cart cart) {

        double total = 0;

        for (CartItem item : cart.getItems()) {
            total += item.getSubtotal();
        }

        return total;
    }

    public void displayCart(Cart cart) {

        System.out.println("===== GIO HANG =====");

        for (CartItem item : cart.getItems()) {

            System.out.println(
                    item.getFood().getFoodName()
                            + " | SL: "
                            + item.getQuantity()
                            + " | Thanh tien: "
                            + item.getSubtotal());
        }

        System.out.println(
                "Tong tien: " + getTotal(cart));
    }
}