package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// BÁO CHO SPRING BIẾT ĐÂY LÀ FILE KHỞI ĐỘNG
// scanBasePackages giúp Spring tìm thấy các thư mục repository, service, api của bạn
@SpringBootApplication(scanBasePackages = {"repository", "service", "api"})
public class Main {

    public static void main(String[] args) {
        /* Bây giờ Spring Boot sẽ tự động quản lý việc khởi tạo Repo và Service
           thông qua các nhãn @Repository và @Service bạn đã gắn,
        */
        // KHỞI ĐỘNG SERVER API
        SpringApplication.run(Main.class, args);

        System.out.println("============================================");
        System.out.println("  API SERVER ĐANG CHẠY TẠI CỔNG 8080   ");
        System.out.println("============================================");
    }
}