package repository;
import org.springframework.stereotype.Repository;
import model.Food;
import utils.FileHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository quản lý dữ liệu món ăn.
 * Lưu/đọc từ file CSV: data/foods.csv
 */
@Repository
public class FoodRepository implements repository.Repository<Food, String> {
    /**repo.Repo để cho khong bị nhầm @Repository của Spring */

    private static final String FILE_PATH = "data/foods.csv";

    // Cache trong bộ nhớ để tránh đọc file liên tục
    private final Map<String, Food> cache = new LinkedHashMap<>();
    private boolean loaded = false;

    /** Đảm bảo dữ liệu đã được load từ file */
    private void ensureLoaded() {
        if (!loaded) {
            loadFromFile();
            loaded = true;
        }
    }

    @Override
    public void save(Food food) {
        ensureLoaded();
        cache.put(food.getFoodId(), food);
        saveToFile();
    }

    @Override
    public Food findById(String foodId) {
        ensureLoaded();
        return cache.get(foodId);
    }

    @Override
    public List<Food> findAll() {
        ensureLoaded();
        return new ArrayList<>(cache.values());
    }

    @Override
    public void deleteById(String foodId) {
        ensureLoaded();
        cache.remove(foodId);
        saveToFile();
    }

    /** Đọc dữ liệu từ file CSV */
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            // Tạo dữ liệu mẫu nếu file chưa tồn tại
            initSampleData();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;

                try {
                    Food food = new Food(
                            parts[0].trim(),
                            parts[1].trim(),
                            Double.parseDouble(parts[2].trim()),
                            parts[3].trim(),
                            Boolean.parseBoolean(parts[4].trim())
                    );
                    cache.put(food.getFoodId(), food);
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi đọc dữ liệu món ăn: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc file foods.csv: " + e.getMessage());
        }
    }

    /** Ghi toàn bộ cache xuống file CSV */
    private void saveToFile() {
        FileHelper.ensureDirectoryExists(FILE_PATH);
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Food food : cache.values()) {
                writer.println(food.toString());
            }
        } catch (IOException e) {
            System.err.println("Lỗi ghi file foods.csv: " + e.getMessage());
        }
    }

    /** Khởi tạo dữ liệu mẫu cho lần chạy đầu tiên */
    private void initSampleData() {
        List<Food> samples = List.of(
                new Food("F001", "Cơm sườn nướng",    35000, "Cơm",      true),
                new Food("F002", "Cơm gà xối mỡ",     32000, "Cơm",      true),
                new Food("F003", "Bún bò Huế",         30000, "Bún",      true),
                new Food("F004", "Phở bò tái",         30000, "Phở",      true),
                new Food("F005", "Bánh mì thịt",       15000, "Bánh mì",  true),
                new Food("F006", "Trà sữa trân châu",  25000, "Đồ uống",  true),
                new Food("F007", "Nước cam tươi",      20000, "Đồ uống",  true),
                new Food("F008", "Cơm chiên dương châu", 28000, "Cơm",   false)
        );
        for (Food f : samples) {
            cache.put(f.getFoodId(), f);
        }
        saveToFile();
        System.out.println("Đã khởi tạo dữ liệu món ăn mẫu.");
    }
}
