package DatHang;

import java.time.LocalDateTime;
import java.util.UUID;

public class datHang {

    public void datHang(DonHang donHang) throws Exception {

        if (donHang.getGioHang() == null) {
            throw new Exception();
        }

        if (donHang.getSoDuTaiKhoan() < donHang.getTongTien()) {
            throw new Exception();
        }

        double soDuMoi = donHang.getSoDuTaiKhoan() - donHang.getTongTien();
        donHang.setSoDuTaiKhoan(soDuMoi);

        String maTuDong = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        donHang.setMaDonHang(maTuDong);
        donHang.setThoiGianDatHang(LocalDateTime.now());
        donHang.setTrangThaiThanhToan("Đã thanh toán");

        donHang.hienThi();
        System.out.println("Đặt hàng và xử lý thanh toán thành công!");
    }
}