package com.travishuy.restaurant_manager.restaurant_manager.model;

import lombok.Data;

@Data
public class User {
    private String name;
    private String address;

    public static void main(String[] args) {
        User u1 = new User();
        u1.setAddress("Hồ Chí Minh");
        u1.setName("Hồ Nhật Huy");
        System.out.println(u1);
    }
}
