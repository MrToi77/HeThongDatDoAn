package QuanLyMonAn;

public class MonChinh extends MonAn {

    private String loaiMon;

    public MonChinh(String maMon,
                    String tenMon,
                    double giaTien,
                    boolean conHang,
                    String loaiMon) {

        super(maMon, tenMon, giaTien, conHang);
        this.loaiMon = loaiMon;
    }

    public String getLoaiMon() {
        return loaiMon;
    }

    @Override
    public void hienThiThongTin() {

        System.out.println(
                "Mon Chinh" +
                        "\nMa Mon: " + getMaMon() +
                        "\nTen Mon: " + getTenMon() +
                        "\nGia Tien: " + getGiaTien() +
                        "\nLoai Mon: " + loaiMon +
                        "\nTrang Thai: " +
                        (isConHang() ? "Con Hang" : "Het Hang")
        );

        System.out.println("-------------------");
    }
}