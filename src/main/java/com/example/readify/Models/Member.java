package com.example.readify.Models;

import java.time.LocalDate;

public class Member {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String membership;
    private LocalDate joinDate;
    private String password;

    public Member(int id,String name, String email, String phone, String address,
                  String membership, LocalDate joinDate, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.membership = membership;
        this.joinDate = joinDate;
        this.password = password;
    }

    public Member(String name, String email, String phone, String address,
                  String membership, LocalDate joinDate, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.membership = membership;
        this.joinDate = joinDate;
        this.password = password;
    }

    public int getId() { return id; }
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getMembership() {
        return membership;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
