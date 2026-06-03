package qlytaikhoan;

public class MonAn {
    private int id;
    private String ten;
    private double cost;

    public MonAn(int id, String ten, double cost) {
        this.id = id;
        this.ten = ten;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return ten + "-" + cost + " $" ;
    }

}
