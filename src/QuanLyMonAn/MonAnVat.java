package QuanLyMonAn;

public class MonAnVat extends MonAn {

    private String kichThuoc;

    public MonAnVat(String maMon,
                    String tenMon,
                    double giaTien,
                    boolean conHang,
                    String kichThuoc) {

        super(maMon, tenMon, giaTien, conHang);
        this.kichThuoc = kichThuoc;
    }

    public String getKichThuoc() {
        return kichThuoc;
    }

    @Override
    public void hienThiThongTin() {

        System.out.println(
                "Mon An Vat" +
                        "\nMa Mon: " + maMon +
                        "\nTen Mon: " + tenMon +
                        "\nGia Tien: " + giaTien +
                        "\nKich Thuoc: " + kichThuoc +
                        "\nTrang Thai: " +
                        (conHang ? "Con Hang" : "Het Hang")
        );

        System.out.println("---------------------");
    }
}