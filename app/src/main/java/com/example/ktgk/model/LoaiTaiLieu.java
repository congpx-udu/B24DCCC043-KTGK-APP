package com.example.ktgk.model;

public enum LoaiTaiLieu {
    BOOK("Book"),
    MAGAZINE("Magazine"),
    EBOOK("EBook");

    private final String tenHienThi;

    LoaiTaiLieu(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }
}
