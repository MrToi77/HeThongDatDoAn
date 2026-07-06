package repository;

import model.Admin;
import utils.FileHelper;

import java.io.*;
import java.util.*;

public class AdminRepository {

    private static final String FILE_PATH = "data/admins.csv";
    private final Map<String, Admin> cache = new LinkedHashMap<>();
    private boolean loaded = false;

    private void ensureLoaded() {
        if (!loaded) { loadFromFile(); loaded = true; }
    }

    public void save(Admin admin) {
        ensureLoaded();
        cache.put(admin.getId(), admin);
        saveToFile();
    }

    public Admin findByPhone(String phone) {
        ensureLoaded();
        return cache.values().stream()
                .filter(a -> a.getPhoneNumber().equals(phone))
                .findFirst().orElse(null);
    }

    public List<Admin> findAll() {
        ensureLoaded();
        return new ArrayList<>(cache.values());
    }

    public void deleteById(String id) {
        ensureLoaded();
        cache.remove(id);
        saveToFile();
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) { initDefaultAdmin(); return; }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(",", -1);
                if (p.length < 3) continue;
                Admin admin = new Admin(p[0].trim(), p[1].trim(), p[2].trim());
                cache.put(admin.getId(), admin);
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc file admins.csv: " + e.getMessage());
        }
    }

    private void saveToFile() {
        FileHelper.ensureDirectoryExists(FILE_PATH);
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Admin a : cache.values()) writer.println(a.toString());
        } catch (IOException e) {
            System.err.println("Lỗi ghi file admins.csv: " + e.getMessage());
        }
    }

    private void initDefaultAdmin() {
        // Tài khoản admin mặc định: SĐT 0000000000, mật khẩu admin123
        Admin defaultAdmin = new Admin("ADMIN001", "0000000000", "admin123");
        cache.put(defaultAdmin.getId(), defaultAdmin);
        saveToFile();
        System.out.println("Đã tạo tài khoản admin mặc định: SĐT=0000000000, mật khẩu=admin123");
    }
}
