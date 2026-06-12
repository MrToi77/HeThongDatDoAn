package service;

import model.Food;
import org.springframework.stereotype.Service;
import repository.FoodRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý logic nghiệp vụ liên quan đến món ăn.
 */
@Service
public class FoodService {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    /**
     * Lấy toàn bộ danh sách món ăn.
     */
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    /**
     * Tìm kiếm món ăn theo tên (không phân biệt hoa thường).
     *
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách món phù hợp
     */
    public List<Food> searchByName(String keyword) {
        String lower = keyword.trim().toLowerCase();
        return foodRepository.findAll().stream()
                .filter(f -> f.getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    /**
     * Tìm món ăn theo mã.
     *
     * @param foodId Mã món
     * @return Food tìm được hoặc null
     */
    public Food findById(String foodId) {
        return foodRepository.findById(foodId);
    }

    /**
     * Lấy danh sách món còn hàng.
     */
    public List<Food> getAvailableFoods() {
        return foodRepository.findAll().stream()
                .filter(Food::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * Hiển thị toàn bộ danh sách món ăn.
     */
    public void displayAllFoods() {
        List<Food> foods = getAllFoods();
        if (foods.isEmpty()) {
            System.out.println("Chưa có món ăn nào trong hệ thống.");
            return;
        }
        System.out.println("===== DANH SÁCH MÓN ĂN =====");
        System.out.printf("%-8s | %-25s | %10s | %-15s | %s%n",
                "Mã món", "Tên món", "Giá (VND)", "Loại", "Trạng thái");
        System.out.println("-".repeat(80));
        for (Food food : foods) {
            food.displayInfo();
        }
        System.out.println("=============================");
    }

    /**
     * Thêm món ăn mới.
     *
     * @param food Món ăn cần thêm
     */
    public void addFood(Food food) {
        foodRepository.save(food);
    }

    /**
     * Cập nhật trạng thái còn/hết hàng của món.
     *
     * @param foodId    Mã món
     * @param available true = còn hàng
     */
    public void updateAvailability(String foodId, boolean available) {
        Food food = foodRepository.findById(foodId);
        if (food == null) {
            throw new IllegalArgumentException("Không tìm thấy món với mã: " + foodId);
        }
        food.setAvailable(available);
        foodRepository.save(food);
        System.out.println("Đã cập nhật trạng thái món: " + food.getName()
                + " -> " + (available ? "Còn hàng" : "Hết hàng"));
    }
}
