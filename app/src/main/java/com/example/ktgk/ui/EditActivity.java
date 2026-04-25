package com.example.ktgk.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ktgk.R;
import com.example.ktgk.data.DocumentRepository;
import com.example.ktgk.model.Book;
import com.example.ktgk.model.Document;
import com.example.ktgk.model.Ebook;
import com.example.ktgk.model.LoaiTaiLieu;
import com.example.ktgk.model.Magazine;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    public static final String EXTRA_MA = "extra_ma";

    private DocumentRepository repo;

    private MaterialToolbar toolbar;
    private MaterialAutoCompleteTextView acLoai;
    private TextInputLayout tilSoTrang, tilSoKy, tilDungLuong;
    private TextInputEditText etMa, etTen, etGia, etSoTrang, etSoKy, etDungLuong;

    private final List<LoaiTaiLieu> loaiValues = new ArrayList<>();
    private LoaiTaiLieu loaiDangChon = LoaiTaiLieu.BOOK;
    private String maCu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        repo = DocumentRepository.getInstance(this);

        toolbar = findViewById(R.id.toolbar);
        acLoai = findViewById(R.id.acLoai);
        etMa = findViewById(R.id.etMa);
        etTen = findViewById(R.id.etTen);
        etGia = findViewById(R.id.etGia);
        tilSoTrang = findViewById(R.id.tilSoTrang);
        etSoTrang = findViewById(R.id.etSoTrang);
        tilSoKy = findViewById(R.id.tilSoKy);
        etSoKy = findViewById(R.id.etSoKy);
        tilDungLuong = findViewById(R.id.tilDungLuong);
        etDungLuong = findViewById(R.id.etDungLuong);
        MaterialButton btnHuy = findViewById(R.id.btnHuy);
        MaterialButton btnLuu = findViewById(R.id.btnLuu);

        toolbar.setNavigationOnClickListener(v -> finish());
        khoiTaoLoai();

        maCu = getIntent().getStringExtra(EXTRA_MA);
        if (maCu != null) {
            toolbar.setTitle(R.string.title_sua);
            napDuLieu(maCu);
        } else {
            toolbar.setTitle(R.string.title_them);
            chonLoai(LoaiTaiLieu.BOOK);
        }

        btnHuy.setOnClickListener(v -> finish());
        btnLuu.setOnClickListener(v -> luu());
    }

    private void khoiTaoLoai() {
        loaiValues.clear();
        List<String> labels = new ArrayList<>();
        for (LoaiTaiLieu l : LoaiTaiLieu.values()) {
            loaiValues.add(l);
            labels.add(l.getTenHienThi());
        }
        ArrayAdapter<String> ad = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, labels);
        acLoai.setAdapter(ad);
        acLoai.setOnItemClickListener((parent, view, position, id) -> chonLoai(loaiValues.get(position)));
    }

    private void chonLoai(LoaiTaiLieu loai) {
        loaiDangChon = loai;
        acLoai.setText(loai.getTenHienThi(), false);
        tilSoTrang.setVisibility(loai == LoaiTaiLieu.BOOK ? View.VISIBLE : View.GONE);
        tilSoKy.setVisibility(loai == LoaiTaiLieu.MAGAZINE ? View.VISIBLE : View.GONE);
        tilDungLuong.setVisibility(loai == LoaiTaiLieu.EBOOK ? View.VISIBLE : View.GONE);
    }

    private void napDuLieu(String ma) {
        Document d = repo.findByMa(ma);
        if (d == null) {
            Toast.makeText(this, "Không tìm thấy tài liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        chonLoai(d.getLoai());
        acLoai.setEnabled(false);
        etMa.setText(d.getMaTaiLieu());
        etTen.setText(d.getTenTaiLieu());
        etGia.setText(String.valueOf(d.getGiaTien()));
        if (d instanceof Book) {
            etSoTrang.setText(String.valueOf(((Book) d).getSoTrang()));
        } else if (d instanceof Magazine) {
            etSoKy.setText(String.valueOf(((Magazine) d).getSoKyPhatHanh()));
        } else if (d instanceof Ebook) {
            etDungLuong.setText(String.valueOf(((Ebook) d).getDungLuongFile()));
        }
    }

    private void luu() {
        try {
            String ma = textOf(etMa);
            String ten = textOf(etTen);
            double gia = parseDouble(textOf(etGia), "Giá tiền");

            Document moi;
            switch (loaiDangChon) {
                case BOOK:
                    int soTrang = parseInt(textOf(etSoTrang), "Số trang");
                    moi = new Book(ma, ten, gia, soTrang);
                    break;
                case MAGAZINE:
                    int soKy = parseInt(textOf(etSoKy), "Số kỳ phát hành");
                    moi = new Magazine(ma, ten, gia, soKy);
                    break;
                case EBOOK:
                default:
                    double dungLuong = parseDouble(textOf(etDungLuong), "Dung lượng file");
                    moi = new Ebook(ma, ten, gia, dungLuong);
                    break;
            }

            if (maCu == null) {
                repo.them(moi);
                Toast.makeText(this, "Đã thêm", Toast.LENGTH_SHORT).show();
            } else {
                repo.capNhat(maCu, moi);
                Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
            }
            finish();
        } catch (IllegalArgumentException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String textOf(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private double parseDouble(String s, String ten) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            throw new IllegalArgumentException(ten + " không hợp lệ");
        }
    }

    private int parseInt(String s, String ten) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            throw new IllegalArgumentException(ten + " không hợp lệ");
        }
    }
}
