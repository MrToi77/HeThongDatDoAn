package model;

/**
 * Lớp Admin kế thừa từ User.
 * Admin quản lý hệ thống: món ăn, sinh viên, đơn hàng.
 */
public class Admin extends User {

    private String password;

    public Admin(String id, String phoneNumber, String password) {
        super(id, "Admin", phoneNumber);
        this.password = password;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public void displayInfo() {
        System.out.println("===== Thông tin Admin =====");
        System.out.println("ID    : " + getId());
        System.out.println("SĐT   : " + getPhoneNumber());
        System.out.println("===========================");
    }

    @Override
    public String toString() {
        // id,phone,password
        return getId() + "," + getPhoneNumber() + "," + password;
    }
}
