package com.example.ktgk.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ktgk.R;
import com.example.ktgk.data.DocumentRepository;
import com.example.ktgk.model.Book;
import com.example.ktgk.model.Document;
import com.example.ktgk.model.Ebook;
import com.example.ktgk.model.Magazine;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MA = "extra_ma";

    private static final NumberFormat MONEY_FORMAT = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        ImageView ivBadge = findViewById(R.id.ivBadge);
        TextView tvLoai = findViewById(R.id.tvLoai);
        TextView tvMa = findViewById(R.id.tvMa);
        TextView tvTen = findViewById(R.id.tvTen);
        TextView tvGia = findViewById(R.id.tvGia);
        TextView tvLabelRieng = findViewById(R.id.tvLabelRieng);
        TextView tvGiaTriRieng = findViewById(R.id.tvGiaTriRieng);
        TextView tvPhi = findViewById(R.id.tvPhi);
        MaterialButton btnDong = findViewById(R.id.btnDong);

        String ma = getIntent().getStringExtra(EXTRA_MA);
        Document d = DocumentRepository.getInstance(this).findByMa(ma);
        if (d == null) {
            Toast.makeText(this, "Không tìm thấy tài liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        switch (d.getLoai()) {
            case BOOK:
                ivBadge.setBackgroundResource(R.drawable.bg_badge_book);
                ivBadge.setImageResource(R.drawable.ic_book_24);
                break;
            case MAGAZINE:
                ivBadge.setBackgroundResource(R.drawable.bg_badge_magazine);
                ivBadge.setImageResource(R.drawable.ic_magazine_24);
                break;
            case EBOOK:
                ivBadge.setBackgroundResource(R.drawable.bg_badge_ebook);
                ivBadge.setImageResource(R.drawable.ic_ebook_24);
                break;
        }

        tvLoai.setText(d.getLoai().getTenHienThi().toUpperCase(Locale.ROOT));
        tvMa.setText("Mã: " + d.getMaTaiLieu());
        tvTen.setText(d.getTenTaiLieu());
        tvGia.setText(MONEY_FORMAT.format(d.getGiaTien()) + " ₫");

        if (d instanceof Book) {
            tvLabelRieng.setText(R.string.label_so_trang);
            tvGiaTriRieng.setText(String.valueOf(((Book) d).getSoTrang()));
        } else if (d instanceof Magazine) {
            tvLabelRieng.setText(R.string.label_so_ky);
            tvGiaTriRieng.setText(String.valueOf(((Magazine) d).getSoKyPhatHanh()));
        } else if (d instanceof Ebook) {
            tvLabelRieng.setText(R.string.label_dung_luong);
            tvGiaTriRieng.setText(((Ebook) d).getDungLuongFile() + " MB");
        }

        tvPhi.setText(MONEY_FORMAT.format(d.tinhPhiMuon()) + " ₫");

        btnDong.setOnClickListener(v -> finish());
    }
}
