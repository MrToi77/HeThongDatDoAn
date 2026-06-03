package qlytaikhoan;

import java.util.ArrayList;
import java.util.List;

public abstract class TaiKhoan implements thanhToan,hienThiMenu {
    private int id;
    private String name;
    private int phone;
    private String address;
    private double balance;

    private List<MonAn> menu = new ArrayList<>();

    public TaiKhoan(int id, String name, int phone, String address, double balance) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.balance = balance;
        this.menu = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getBalance() {
        return balance;
    }

    void setBalance(double balance) {
        this.balance = balance;
    }

    void xemMenu(List<MonAn> menu) {
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i).toString());
        }
    }

    void addMenu(MonAn monAn) {
        menu.add(monAn);
        System.out.println("added " + monAn.getTen() + " to menu");
    }

    @Override
    public double datHang() {
        if (menu.size() == 0) {
            throw new IllegalArgumentException();
        }
        double sum = 0;
        System.out.println("-------------------------");
        for (MonAn mon : menu) {
            System.out.println("dish: " + mon.getTen());
            System.out.println("cost: " + mon.getCost());
            sum += mon.getCost();

        }
        System.out.println("-------------------------");
        System.out.println("total: " + sum);
        return sum;
    }


     @Override
     public double tinhTien(double total) {
        if (total > balance) {
            throw new IllegalArgumentException();
        } else {
            balance -= total;
            System.out.println("paid " + total);
        }
         return total;
     }

}
