package QuanLyMonAn;

import java.util.ArrayList;

public class QuanLyMonAn {

    private ArrayList<MonAn> danhSachMon;

    public QuanLyMonAn() {
        danhSachMon = new ArrayList<>();
    }

    public void themMon(MonAn mon) {
        danhSachMon.add(mon);
    }

    public void hienThiDanhSach() {

        for (MonAn mon : danhSachMon) {
            mon.hienThiThongTin();
        }
    }

    public void timKiemTheoTen(String ten) {

        boolean timThay = false;

        for (MonAn mon : danhSachMon) {

            if (mon.getTenMon()
                    .toLowerCase()
                    .contains(ten.toLowerCase())) {

                mon.hienThiThongTin();
                timThay = true;
            }
        }

        if (!timThay) {
            System.out.println("Khong tim thay mon.");
        }
    }
}