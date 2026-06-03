package QuanLyMonAn;

public class MonTrangMieng extends MonAn {

    private String huongVi;

    public MonTrangMieng(String maMon,
                         String tenMon,
                         double giaTien,
                         boolean conHang,
                         String huongVi) {

        super(maMon, tenMon, giaTien, conHang);
        this.huongVi = huongVi;
    }

    public String getHuongVi() {
        return huongVi;
    }

    @Override
    public void hienThiThongTin() {

        System.out.println(
                "Mon Trang Mieng" +
                        "\nMa Mon: " + maMon +
                        "\nTen Mon: " + tenMon +
                        "\nGia Tien: " + giaTien +
                        "\nHuong Vi: " + huongVi +
                        "\nTrang Thai: " +
                        (conHang ? "Con Hang" : "Het Hang")
        );

        System.out.println("---------------------");
    }
}