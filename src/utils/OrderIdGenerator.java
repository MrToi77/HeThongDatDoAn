package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tiện ích sinh mã đơn hàng tự động.
 * Định dạng: ORD-yyyyMMdd-XXXX (XXXX là số thứ tự tăng dần)
 */
public class OrderIdGenerator {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    // Bộ đếm tự động tăng (thread-safe)
    private static final AtomicInteger counter = new AtomicInteger(1);

    /** Không cho phép khởi tạo */
    private OrderIdGenerator() {}

    /**
     * Sinh mã đơn hàng mới.
     *
     * @return Mã đơn hàng theo định dạng ORD-yyyyMMdd-XXXX
     */
    public static String generate() {
        String date = LocalDateTime.now().format(DATE_FORMAT);
        String seq  = String.format("%04d", counter.getAndIncrement());
        return "ORD-" + date + "-" + seq;
    }

    /**
     * Đặt lại bộ đếm (dùng cho testing).
     *
     * @param value Giá trị khởi đầu
     */
    public static void resetCounter(int value) {
        counter.set(value);
    }
}
