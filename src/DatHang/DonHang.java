package DatHang;

import java.time.LocalDateTime;

public class DonHang {
    private String maDonHang;
    private LocalDateTime thoiGianDatHang;
    private double tongTien;
    private String trangThaiThanhToan;
    private double soDuTaiKhoan;
    private String gioHang;

    public DonHang() {
    }

    public DonHang(String maDonHang, LocalDateTime thoiGianDatHang, double tongTien,
                   String trangThaiThanhToan, String gioHang, double soDuTaiKhoan) {
        this.maDonHang = maDonHang;
        this.thoiGianDatHang = thoiGianDatHang;
        this.tongTien = tongTien;
        this.trangThaiThanhToan = trangThaiThanhToan;
        this.gioHang = gioHang;
        this.soDuTaiKhoan = soDuTaiKhoan;
    }

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        if (tongTien >=0 ) this.tongTien = tongTien;
        else throw new IllegalArgumentException();
    }

    public LocalDateTime getThoiGianDatHang() {
        return thoiGianDatHang;
    }

    public void setThoiGianDatHang(LocalDateTime thoiGianDatHang) {
        this.thoiGianDatHang = thoiGianDatHang;
    }

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(String trangThaiThanhToan) {
        if (trangThaiThanhToan != null ) this.trangThaiThanhToan = trangThaiThanhToan;
        else throw new IllegalArgumentException();
    }

    public double getSoDuTaiKhoan() {
        return soDuTaiKhoan;
    }

    public void setSoDuTaiKhoan(double soDuTaiKhoan) {
        if (soDuTaiKhoan >= 0 ) this.soDuTaiKhoan = soDuTaiKhoan;
        else throw new IllegalArgumentException();
    }

    public String getGioHang() {
        return gioHang;
    }

    public void setGioHang(String gioHang) {
        this.gioHang = gioHang;
    }

    public void hienThi() {
        System.out.println("Ma Don Hang: " + maDonHang);
        System.out.println("Thoi Gian Dat Hang: " + thoiGianDatHang);
        System.out.println("Tong Tien: " + tongTien + " VND");
        System.out.println("Trang Thai Don Hang: " + trangThaiThanhToan);
    }
}