package com.example.ktgk.model;

import java.io.Serializable;

public abstract class Document implements Serializable {

    private String maTaiLieu;
    private String tenTaiLieu;
    private double giaTien;

    public Document(String maTaiLieu, String tenTaiLieu, double giaTien) {
        setMaTaiLieu(maTaiLieu);
        setTenTaiLieu(tenTaiLieu);
        setGiaTien(giaTien);
    }

    public String getMaTaiLieu() {
        return maTaiLieu;
    }

    public void setMaTaiLieu(String maTaiLieu) {
        if (maTaiLieu == null || maTaiLieu.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã tài liệu không được để trống");
        }
        this.maTaiLieu = maTaiLieu.trim();
    }

    public String getTenTaiLieu() {
        return tenTaiLieu;
    }

    public void setTenTaiLieu(String tenTaiLieu) {
        if (tenTaiLieu == null || tenTaiLieu.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên tài liệu không được để trống");
        }
        this.tenTaiLieu = tenTaiLieu.trim();
    }

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
        if (giaTien <= 0) {
            throw new IllegalArgumentException("Giá tiền phải lớn hơn 0");
        }
        this.giaTien = giaTien;
    }

    public abstract double tinhPhiMuon();

    public abstract String hienThiThongTin();

    public abstract LoaiTaiLieu getLoai();
}
