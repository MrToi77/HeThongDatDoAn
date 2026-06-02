package QuanLyMonAn;

public class InitQuanLyMonAn {

    public static void main(String[] args) {

        QuanLyMonAn ql = new QuanLyMonAn();

        MonAn mon1 =
                new MonChinh(
                        "M01",
                        "Com Ga",
                        35000,
                        true,
                        "Com"
                );

        MonAn mon2 =
                new DoUong(
                        "D01",
                        "Tra Dao",
                        25000,
                        false,
                        "500ml"
                );

        ql.themMon(mon1);
        ql.themMon(mon2);

        System.out.println("===== DANH SACH =====");

        ql.hienThiDanhSach();

        System.out.println("\n===== TIM KIEM =====");

        ql.timKiemTheoTen("Com");

        System.out.println("\n===== DAT MON =====");

        DatMon dat1 = new DatMonTaiCho();
        DatMon dat2 = new DatMonMangVe();

        dat1.datMon(mon1);
        dat2.datMon(mon2);
    }
}