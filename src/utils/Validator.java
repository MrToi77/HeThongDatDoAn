package utils;

/**
 * Tiện ích kiểm tra / validate dữ liệu đầu vào.
 */
public class Validator {

    /** Không cho phép khởi tạo */
    private Validator() {}

    /**
     * Kiểm tra chuỗi không null và không rỗng.
     *
     * @param value     Giá trị cần kiểm tra
     * @param fieldName Tên trường (dùng trong thông báo lỗi)
     */
    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " không được để trống!");
        }
    }

    /**
     * Kiểm tra số tiền không âm.
     *
     * @param amount    Số tiền
     * @param fieldName Tên trường
     */
    public static void requireNonNegativeAmount(double amount, String fieldName) {
        if (amount < 0) {
            throw new IllegalArgumentException(fieldName + " không được âm!");
        }
    }

    /**
     * Kiểm tra số lượng hợp lệ (>= 1).
     *
     * @param quantity  Số lượng
     * @param fieldName Tên trường
     */
    public static void requirePositiveQuantity(int quantity, String fieldName) {
        if (quantity < 1) {
            throw new IllegalArgumentException(fieldName + " phải >= 1!");
        }
    }

    /**
     * Kiểm tra định dạng số điện thoại Việt Nam (10 chữ số).
     *
     * @param phone Số điện thoại
     * @return true nếu hợp lệ
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^(0[3-9]\\d{8})$");
    }
}
