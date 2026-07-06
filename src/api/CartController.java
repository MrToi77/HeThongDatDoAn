package api;

import api.dto.AddToCartRequest;
import model.Cart;
import model.Food;
import org.springframework.web.bind.annotation.*;
import service.CartService;
import service.FoodService;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;
    private final FoodService foodService;

    public CartController(CartService cartService, FoodService foodService) {
        this.cartService = cartService;
        this.foodService = foodService;
    }

    // API: Xem giỏ hàng của một sinh viên
    @GetMapping("/{studentId}")
    public Cart viewCart(@PathVariable String studentId) {
        return cartService.getCartByStudentId(studentId);
    }

    // API: Thêm món vào giỏ
    @PostMapping("/{studentId}/add")
    public Cart addToCart(@PathVariable String studentId, @RequestBody AddToCartRequest request) {
        Cart cart = cartService.getCartByStudentId(studentId);
        Food food = foodService.findById(request.getFoodId());

        cartService.addToCart(cart, food, request.getQuantity());
        return cart; // Trả về giỏ hàng mới nhất
    }

    // API: Xóa toàn bộ giỏ hàng
    @DeleteMapping("/{studentId}/clear")
    public String clearCart(@PathVariable String studentId) {
        Cart cart = cartService.getCartByStudentId(studentId);
        cartService.clearCart(cart);
        return "Đã xóa sạch giỏ hàng của " + studentId;
    }
}