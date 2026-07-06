package api;

import model.Food;
import org.springframework.web.bind.annotation.*;
import repository.FoodRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/foods") // Đường dẫn gốc cho Food API
public class FoodController {

    private final FoodRepository foodRepository;

    // Spring Boot sẽ tự động tiêm (inject) FoodRepository vào đây
    public FoodController(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    // API: Lấy danh sách toàn bộ món ăn
    // Đường dẫn: GET http://localhost:8080/api/v1/foods
    @GetMapping
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    // API: Lấy thông tin 1 món ăn cụ thể theo ID
    // Đường dẫn: GET http://localhost:8080/api/v1/foods/F001
    @GetMapping("/{id}")
    public Food getFoodById(@PathVariable String id) {
        return foodRepository.findById(id);
    }
}