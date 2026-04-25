package com.example.ktgk.model;

public class Book extends Document {

    private int soTrang;

    public Book(String maTaiLieu, String tenTaiLieu, double giaTien, int soTrang) {
        super(maTaiLieu, tenTaiLieu, giaTien);
        setSoTrang(soTrang);
    }

    public int getSoTrang() {
        return soTrang;
    }

    public void setSoTrang(int soTrang) {
        if (soTrang <= 0) {
            throw new IllegalArgumentException("Số trang phải lớn hơn 0");
        }
        this.soTrang = soTrang;
    }

    @Override
    public double tinhPhiMuon() {
        return getGiaTien() * 0.05;
    }

    @Override
    public LoaiTaiLieu getLoai() {
        return LoaiTaiLieu.BOOK;
    }

    @Override
    public String hienThiThongTin() {
        return "[Book] " + getMaTaiLieu() + " - " + getTenTaiLieu()
                + " | Giá: " + getGiaTien()
                + " | Số trang: " + soTrang
                + " | Phí mượn: " + tinhPhiMuon();
    }
}
