package model;

/**
 * Lớp cơ sở đại diện cho người dùng hệ thống.
 * Được kế thừa bởi Student và các loại người dùng khác.
 */
public abstract class User {

    private String id;
    private String fullName;
    private String phoneNumber;

    public User(String id, String fullName, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    // ==================== Getters & Setters ====================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Hiển thị thông tin người dùng (bắt buộc override ở lớp con).
     */
    public abstract void displayInfo();

    @Override
    public String toString() {
        return "User{id='" + id + "', fullName='" + fullName + "', phone='" + phoneNumber + "'}";
    }
}
