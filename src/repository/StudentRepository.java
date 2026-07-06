package repository;

import model.Student;
import org.springframework.stereotype.Repository;
import utils.FileHelper;

import java.io.*;
import java.util.*;

@Repository
public class StudentRepository implements repository.Repository<Student, String> {

    private static final String FILE_PATH = "data/students.csv";
    private final Map<String, Student> cache = new LinkedHashMap<>();
    private boolean loaded = false;

    private void ensureLoaded() {
        if (!loaded) { loadFromFile(); loaded = true; }
    }

    @Override
    public void save(Student student) {
        ensureLoaded();
        cache.put(student.getId(), student);
        saveToFile();
    }

    @Override
    public Student findById(String id) {
        ensureLoaded();
        return cache.get(id);
    }

    @Override
    public List<Student> findAll() {
        ensureLoaded();
        return new ArrayList<>(cache.values());
    }

    @Override
    public void deleteById(String id) {
        ensureLoaded();
        cache.remove(id);
        saveToFile();
    }

    /** Tìm sinh viên theo số điện thoại */
    public Student findByPhone(String phone) {
        ensureLoaded();
        return cache.values().stream()
                .filter(s -> s.getPhoneNumber().equals(phone))
                .findFirst().orElse(null);
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) { initSampleData(); return; }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(",", -1);
                if (p.length < 5) continue;
                try {
                    // id, fullName, phone, room, balance [, password [, activeTime]]
                    String password   = p.length > 5 ? p[5].trim() : "123456";
                    Student student = new Student(
                            p[0].trim(), p[1].trim(), p[2].trim(),
                            p[3].trim(), Double.parseDouble(p[4].trim()), password);
                    if (p.length > 6 && !p[6].trim().isEmpty()) {
                        student.setActiveTime(p[6].trim());
                    }
                    cache.put(student.getId(), student);
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi đọc sinh viên: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc file students.csv: " + e.getMessage());
        }
    }

    private void saveToFile() {
        FileHelper.ensureDirectoryExists(FILE_PATH);
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Student s : cache.values()) writer.println(s.toString());
        } catch (IOException e) {
            System.err.println("Lỗi ghi file students.csv: " + e.getMessage());
        }
    }

    private void initSampleData() {
        List<Student> samples = List.of(
                new Student("SV001", "Nguyen Van An",  "0901234567", "A1-101", 200000, "123456"),
                new Student("SV002", "Tran Thi Bich",  "0912345678", "B2-205", 150000, "123456"),
                new Student("SV003", "Le Hoang Nam",   "0923456789", "A3-310",  50000, "123456")
        );
        for (Student s : samples) cache.put(s.getId(), s);
        saveToFile();
        System.out.println("Đã khởi tạo dữ liệu sinh viên mẫu.");
    }
}
