package QuanLyMonAn;

public class MonAnVat extends MonAn {

    private String cachCheBien;
    private String mucDoCay;

    public MonAnVat(String maMon,
                    String tenMon,
                    double giaTien,
                    boolean conHang,
                    String cachCheBien,
                    String mucDoCay) {

        super(maMon, tenMon, giaTien, conHang);

        this.cachCheBien = cachCheBien;
        this.mucDoCay = mucDoCay;
    }

    public String getCachCheBien() {
        return cachCheBien;
    }

    public String getMucDoCay() {
        return mucDoCay;
    }

    @Override
    public void hienThiThongTin() {

        System.out.println(
                "=== MON AN VAT ===" +
                        "\nMa Mon: " + getMaMon() +
                        "\nTen Mon: " + getTenMon() +
                        "\nGia Tien: " + getGiaTien() +
                        "\nCach Che Bien: " + cachCheBien +
                        "\nMuc Do Cay: " + mucDoCay +
                        "\nTrang Thai: " +
                        (isConHang() ? "Con Hang" : "Het Hang")
        );

        System.out.println("----------------------");
    }
}