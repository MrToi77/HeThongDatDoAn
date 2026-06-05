package service;

import model.Student;
import repository.StudentRepository;

import java.util.List;

/**
 * Service xử lý logic liên quan đến sinh viên.
 */
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Tìm sinh viên theo mã.
     *
     * @param studentId Mã sinh viên
     * @return Student hoặc null nếu không tìm thấy
     */
    public Student findById(String studentId) {
        return studentRepository.findById(studentId);
    }

    /**
     * Lấy tất cả sinh viên.
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Đăng ký sinh viên mới.
     *
     * @param student Sinh viên cần thêm
     */
    public void registerStudent(Student student) {
        if (studentRepository.findById(student.getId()) != null) {
            throw new IllegalArgumentException("Mã sinh viên '" + student.getId() + "' đã tồn tại!");
        }
        studentRepository.save(student);
        System.out.println("Đăng ký sinh viên thành công: " + student.getFullName());
    }

    /**
     * Nạp tiền vào tài khoản sinh viên.
     *
     * @param studentId Mã sinh viên
     * @param amount    Số tiền nạp
     */
    public void topUpBalance(String studentId, double amount) {
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Không tìm thấy sinh viên: " + studentId);
        }
        student.topUpBalance(amount);
        studentRepository.save(student);
        System.out.printf("Nạp %.0f VND thành công. Số dư mới: %.0f VND%n",
                amount, student.getAccountBalance());
    }
}
