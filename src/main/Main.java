package main;

import api.ApiServer;

public class Main {
    public static void main(String[] args) {
        try {
            ApiServer.start();
        } catch (Exception e) {
            System.out.println("Lỗi khởi động server: " + e.getMessage());
        }
    }
}