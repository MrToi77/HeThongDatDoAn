package QuanLyMonAn;

public class DoUong extends MonAn {

    private String dungTich;

    public DoUong(String maMon,
                  String tenMon,
                  double giaTien,
                  boolean conHang,
                  String dungTich) {

        super(maMon, tenMon, giaTien, conHang);
        this.dungTich = dungTich;
    }

    public String getDungTich() {
        return dungTich;
    }

    @Override
    public void hienThiThongTin() {

        System.out.println(
                "Do Uong" +
                        "\nMa Mon: " + getMaMon() +
                        "\nTen Mon: " + getTenMon() +
                        "\nGia Tien: " + getGiaTien() +
                        "\nDung Tich: " + dungTich +
                        "\nTrang Thai: " +
                        (isConHang() ? "Con Hang" : "Het Hang")
        );

        System.out.println("-------------------");
    }
}