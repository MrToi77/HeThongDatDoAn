package QuanLyMonAn;

public class DatMonTaiCho implements DatMon {

    @Override
    public void datMon(MonAn mon) {

        if (!mon.isConHang()) {

            System.out.println(
                    mon.getTenMon()
                            + " da het hang!"
            );

            return;
        }

        System.out.println(
                "Dat tai cho thanh cong: "
                        + mon.getTenMon()
        );
    }
}