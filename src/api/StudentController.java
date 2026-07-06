package api;

import model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // API: Lấy danh sách toàn bộ sinh viên
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // API: Tạo sinh viên mới
    // POST http://localhost:8080/api/v1/students
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        try {
            studentService.registerStudent(student); // tự validate trùng ID bên trong
            return ResponseEntity.ok(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
