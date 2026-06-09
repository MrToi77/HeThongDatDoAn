package api;

import model.Student;
import org.springframework.web.bind.annotation.*;
import repository.StudentRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students") // Đây chính là "địa chỉ phòng" mà Server đang tìm kiếm
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // API: Lấy danh sách toàn bộ sinh viên
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}