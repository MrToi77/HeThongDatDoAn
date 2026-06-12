package utils;

import java.io.File;

/**
 * Tiện ích hỗ trợ thao tác file.
 */
public class FileHelper {

    /** Không cho phép khởi tạo */
    private FileHelper() {}

    /**
     * Đảm bảo thư mục chứa file tồn tại.
     * Nếu chưa tồn tại, tự động tạo.
     *
     * @param filePath Đường dẫn file
     */
    public static void ensureDirectoryExists(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    /**
     * Kiểm tra file có tồn tại và đọc được không.
     *
     * @param filePath Đường dẫn file
     * @return true nếu tồn tại
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile() && file.canRead();
    }
}
