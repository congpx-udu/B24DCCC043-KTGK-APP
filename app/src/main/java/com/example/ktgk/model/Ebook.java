package com.example.ktgk.model;

public class Ebook extends Document {

    private double dungLuongFile;

    public Ebook(String maTaiLieu, String tenTaiLieu, double giaTien, double dungLuongFile) {
        super(maTaiLieu, tenTaiLieu, giaTien);
        setDungLuongFile(dungLuongFile);
    }

    public double getDungLuongFile() {
        return dungLuongFile;
    }

    public void setDungLuongFile(double dungLuongFile) {
        if (dungLuongFile <= 0) {
            throw new IllegalArgumentException("Dung lượng file phải lớn hơn 0");
        }
        this.dungLuongFile = dungLuongFile;
    }

    @Override
    public double tinhPhiMuon() {
        return dungLuongFile * 1000;
    }

    @Override
    public LoaiTaiLieu getLoai() {
        return LoaiTaiLieu.EBOOK;
    }

    @Override
    public String hienThiThongTin() {
        return "[EBook] " + getMaTaiLieu() + " - " + getTenTaiLieu()
                + " | Giá: " + getGiaTien()
                + " | Dung lượng: " + dungLuongFile + " MB"
                + " | Phí mượn: " + tinhPhiMuon();
    }
}
