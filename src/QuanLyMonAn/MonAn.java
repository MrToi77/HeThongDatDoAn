package QuanLyMonAn;

public abstract class MonAn {

    protected String maMon;
    protected String tenMon;
    protected double giaTien;
    protected boolean conHang;

    public MonAn(String maMon,
                 String tenMon,
                 double giaTien,
                 boolean conHang) {

        this.maMon = maMon;
        this.tenMon = tenMon;
        this.giaTien = giaTien;
        this.conHang = conHang;
    }

    public String getMaMon() {
        return maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public boolean isConHang() {
        return conHang;
    }

    public abstract void hienThiThongTin();
}