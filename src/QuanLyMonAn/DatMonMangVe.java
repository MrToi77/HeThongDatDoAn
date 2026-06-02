package QuanLyMonAn;

public class DatMonMangVe implements DatMon {

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
                "Dat mang ve thanh cong: "
                        + mon.getTenMon()
        );
    }
}