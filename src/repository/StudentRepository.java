package repository;

import model.Student;
import utils.FileHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository quản lý dữ liệu sinh viên.
 * Lưu/đọc từ file CSV: data/students.csv
 */
public class StudentRepository implements Repository<Student, String> {

    private static final String FILE_PATH = "data/students.csv";

    private final Map<String, Student> cache = new LinkedHashMap<>();
    private boolean loaded = false;

    private void ensureLoaded() {
        if (!loaded) {
            loadFromFile();
            loaded = true;
        }
    }

    @Override
    public void save(Student student) {
        ensureLoaded();
        cache.put(student.getId(), student);
        saveToFile();
    }

    @Override
    public Student findById(String studentId) {
        ensureLoaded();
        return cache.get(studentId);
    }

    @Override
    public List<Student> findAll() {
        ensureLoaded();
        return new ArrayList<>(cache.values());
    }

    @Override
    public void deleteById(String studentId) {
        ensureLoaded();
        cache.remove(studentId);
        saveToFile();
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
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
                    Student student = new Student(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim(),
                            Double.parseDouble(parts[4].trim())
                    );
                    cache.put(student.getId(), student);
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi đọc dữ liệu sinh viên: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc file students.csv: " + e.getMessage());
        }
    }

    private void saveToFile() {
        FileHelper.ensureDirectoryExists(FILE_PATH);
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Student student : cache.values()) {
                writer.println(student.toString());
            }
        } catch (IOException e) {
            System.err.println("Lỗi ghi file students.csv: " + e.getMessage());
        }
    }

    private void initSampleData() {
        List<Student> samples = List.of(
                new Student("SV001", "Nguyen Van An",   "0901234567", "A1-101", 200000),
                new Student("SV002", "Tran Thi Bich",   "0912345678", "B2-205", 150000),
                new Student("SV003", "Le Hoang Nam",    "0923456789", "A3-310", 50000)
        );
        for (Student s : samples) {
            cache.put(s.getId(), s);
        }
        saveToFile();
        System.out.println("Đã khởi tạo dữ liệu sinh viên mẫu.");
    }
}
