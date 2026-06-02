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
                        "\nMa Mon: " + maMon +
                        "\nTen Mon: " + tenMon +
                        "\nGia Tien: " + giaTien +
                        "\nLoai Mon: " + loaiMon +
                        "\nTrang Thai: " +
                        (conHang ? "Con Hang" : "Het Hang")
        );

        System.out.println("-------------------");
    }
}