package com.example.ktgk.model;

public class Magazine extends Document {

    private int soKyPhatHanh;

    public Magazine(String maTaiLieu, String tenTaiLieu, double giaTien, int soKyPhatHanh) {
        super(maTaiLieu, tenTaiLieu, giaTien);
        setSoKyPhatHanh(soKyPhatHanh);
    }

    public int getSoKyPhatHanh() {
        return soKyPhatHanh;
    }

    public void setSoKyPhatHanh(int soKyPhatHanh) {
        if (soKyPhatHanh <= 0) {
            throw new IllegalArgumentException("Số kỳ phát hành phải lớn hơn 0");
        }
        this.soKyPhatHanh = soKyPhatHanh;
    }

    @Override
    public double tinhPhiMuon() {
        return getGiaTien() * 0.03;
    }

    @Override
    public LoaiTaiLieu getLoai() {
        return LoaiTaiLieu.MAGAZINE;
    }

    @Override
    public String hienThiThongTin() {
        return "[Magazine] " + getMaTaiLieu() + " - " + getTenTaiLieu()
                + " | Giá: " + getGiaTien()
                + " | Số kỳ phát hành: " + soKyPhatHanh
                + " | Phí mượn: " + tinhPhiMuon();
    }
}
