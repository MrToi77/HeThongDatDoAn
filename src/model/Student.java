package model;

/**
 * Lớp Student kế thừa từ User.
 * Đại diện cho sinh viên trong ký túc xá với số dư tài khoản và địa chỉ phòng.
 */
public class Student extends User {

    private String roomAddress;   // Địa chỉ phòng ký túc xá
    private double accountBalance; // Số dư tài khoản

    public Student(String studentId, String fullName, String phoneNumber,
                   String roomAddress, double accountBalance) {
        super(studentId, fullName, phoneNumber);
        this.roomAddress = roomAddress;
        this.accountBalance = accountBalance;
    }

    // ==================== Getters & Setters ====================

    public String getRoomAddress() {
        return roomAddress;
    }

    public void setRoomAddress(String roomAddress) {
        this.roomAddress = roomAddress;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    /**
     * Trừ tiền khỏi tài khoản sau khi thanh toán thành công.
     *
     * @param amount Số tiền cần trừ
     * @throws IllegalArgumentException nếu số dư không đủ
     */
    public void deductBalance(double amount) {
        if (amount > accountBalance) {
            throw new IllegalArgumentException(
                    "Số dư không đủ! Số dư hiện tại: " + accountBalance + " VND, cần: " + amount + " VND."
            );
        }
        this.accountBalance -= amount;
    }

    /**
     * Nạp tiền vào tài khoản.
     *
     * @param amount Số tiền nạp thêm (phải > 0)
     */
    public void topUpBalance(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền nạp phải lớn hơn 0.");
        }
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
        System.out.println("================================");
    }

    @Override
    public String toString() {
        return getId() + "," + getFullName() + "," + getPhoneNumber() + ","
                + roomAddress + "," + accountBalance;
    }
}
