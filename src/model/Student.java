package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lớp Student kế thừa từ User.
 */
public class Student extends User {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private String roomAddress;
    private double accountBalance;
    private String password;
    private String activeTime; // Thời gian hoạt động gần nhất

    public Student(String studentId, String fullName, String phoneNumber,
                   String roomAddress, double accountBalance, String password) {
        super(studentId, fullName, phoneNumber);
        this.roomAddress    = roomAddress;
        this.accountBalance = accountBalance;
        this.password       = password;
        this.activeTime     = LocalDateTime.now().format(FMT);
    }

    // Constructor tương thích ngược (không có password) — dùng cho dữ liệu mẫu cũ
    public Student(String studentId, String fullName, String phoneNumber,
                   String roomAddress, double accountBalance) {
        this(studentId, fullName, phoneNumber, roomAddress, accountBalance, "123456");
    }

    // ==================== Getters & Setters ====================

    public String getRoomAddress()  { return roomAddress; }
    public void setRoomAddress(String roomAddress) { this.roomAddress = roomAddress; }

    public double getAccountBalance() { return accountBalance; }
    public void setAccountBalance(double accountBalance) { this.accountBalance = accountBalance; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getActiveTime() { return activeTime; }
    public void setActiveTime(String activeTime) { this.activeTime = activeTime; }

    /** Cập nhật thời gian hoạt động về thời điểm hiện tại */
    public void updateActiveTime() {
        this.activeTime = LocalDateTime.now().format(FMT);
    }

    public void deductBalance(double amount) {
        if (amount > accountBalance) {
            throw new IllegalArgumentException(
                "Số dư không đủ! Hiện tại: " + accountBalance + " VND, cần: " + amount + " VND.");
        }
        this.accountBalance -= amount;
    }

    public void topUpBalance(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Số tiền nạp phải lớn hơn 0.");
        this.accountBalance += amount;
    }

    @Override
    public void displayInfo() {
        System.out.println("===== Thông tin sinh viên =====");
        System.out.println("Mã SV      : " + getId());
        System.out.println("Họ tên     : " + getFullName());
        System.out.println("SĐT        : " + getPhoneNumber());
        System.out.println("Phòng KTX  : " + roomAddress);
        System.out.printf("Số dư TK   : %.0f VND%n", accountBalance);
        System.out.println("Hoạt động  : " + activeTime);
        System.out.println("================================");
    }

    @Override
    public String toString() {
        // id,fullName,phone,room,balance,password,activeTime
        return getId() + "," + getFullName() + "," + getPhoneNumber() + ","
                + roomAddress + "," + accountBalance + ","
                + (password != null ? password : "") + ","
                + (activeTime != null ? activeTime : "");
    }
}
