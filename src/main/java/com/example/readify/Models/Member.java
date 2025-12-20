package com.example.readify.Models;

import java.time.LocalDate;

public class Member {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String membership;
    private LocalDate joinDate;
    private String password;

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

    public String getEmail() {
        return email;
    }
}
