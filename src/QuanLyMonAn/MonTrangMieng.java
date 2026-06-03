package QuanLyMonAn;

public class MonTrangMieng extends MonAn {

    private String loaiBanh;
    private String loaiTraiCay;

    public MonTrangMieng(String maMon,
                         String tenMon,
                         double giaTien,
                         boolean conHang,
                         String loaiBanh,
                         String loaiTraiCay) {

        super(maMon, tenMon, giaTien, conHang);

        this.loaiBanh = loaiBanh;
        this.loaiTraiCay = loaiTraiCay;
    }

    public String getLoaiBanh() {
        return loaiBanh;
    }

    public String getLoaiTraiCay() {
        return loaiTraiCay;
    }

    @Override
    public void hienThiThongTin() {

        System.out.println(
                "=== MON TRANG MIENG ===" +
                        "\nMa Mon: " + getMaMon() +
                        "\nTen Mon: " + getTenMon() +
                        "\nGia Tien: " + getGiaTien() +
                        "\nLoai Banh: " + loaiBanh +
                        "\nLoai Trai Cay: " + loaiTraiCay +
                        "\nTrang Thai: " +
                        (isConHang() ? "Con Hang" : "Het Hang")
        );

        System.out.println("----------------------");
    }
}