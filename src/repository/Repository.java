package repository;

import java.util.List;

/**
 * Interface Repository chung định nghĩa các thao tác CRUD cơ bản.
 * Các repository cụ thể kế thừa interface này.
 *
 * @param <T>  Kiểu đối tượng quản lý
 * @param <ID> Kiểu của khóa chính
 */
public interface Repository<T, ID> {

    /**
     * Lưu hoặc cập nhật đối tượng.
     *
     * @param entity Đối tượng cần lưu
     */
    void save(T entity);

    /**
     * Tìm đối tượng theo ID.
     *
     * @param id Khóa chính
     * @return Đối tượng hoặc null
     */
    T findById(ID id);

    /**
     * Lấy toàn bộ danh sách.
     *
     * @return Danh sách đối tượng
     */
    List<T> findAll();

    /**
     * Xóa đối tượng theo ID.
     *
     * @param id Khóa chính
     */
    void deleteById(ID id);
}
