package com.example.ktgk;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ktgk.data.DocumentRepository;
import com.example.ktgk.model.Document;
import com.example.ktgk.model.LoaiTaiLieu;
import com.example.ktgk.ui.DetailActivity;
import com.example.ktgk.ui.DocumentAdapter;
import com.example.ktgk.ui.EditActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int PAGE_SIZE = 5;

    private DocumentAdapter adapter;
    private TextInputEditText etSearch;
    private TextView tvEmpty;

    private View paginationBar;
    private LinearLayout pageNumbersContainer;
    private ImageButton btnFirst, btnPrev, btnNext, btnLast;

    private Chip chipAll, chipBook, chipMagazine, chipEbook;
    private Chip chipSort, chipPhiCaoNhat;

    private DocumentRepository repo;

    private LoaiTaiLieu loaiDangChon = null;
    private List<Document> filteredList = new ArrayList<>();
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        repo = DocumentRepository.getInstance(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, 0, bars.right, bars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = findViewById(R.id.rvDanhSach);
        etSearch = findViewById(R.id.etSearch);
        tvEmpty = findViewById(R.id.tvEmpty);

        paginationBar = findViewById(R.id.paginationBar);
        pageNumbersContainer = findViewById(R.id.pageNumbersContainer);
        btnFirst = findViewById(R.id.btnFirst);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnLast = findViewById(R.id.btnLast);

        chipAll = findViewById(R.id.chipAll);
        chipBook = findViewById(R.id.chipBook);
        chipMagazine = findViewById(R.id.chipMagazine);
        chipEbook = findViewById(R.id.chipEbook);
        chipSort = findViewById(R.id.chipSort);
        chipPhiCaoNhat = findViewById(R.id.chipPhiCaoNhat);

        adapter = new DocumentAdapter(new DocumentAdapter.Listener() {
            @Override
            public void onClick(Document d) {
                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra(DetailActivity.EXTRA_MA, d.getMaTaiLieu());
                startActivity(i);
            }

            @Override
            public void onSua(Document d) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(EditActivity.EXTRA_MA, d.getMaTaiLieu());
                startActivity(i);
            }

            @Override
            public void onXoa(Document d) {
                xacNhanXoa(d);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { capNhatBoLoc(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        chipAll.setOnCheckedChangeListener((b, c) -> { if (c) { loaiDangChon = null; capNhatBoLoc(); } });
        chipBook.setOnCheckedChangeListener((b, c) -> { if (c) { loaiDangChon = LoaiTaiLieu.BOOK; capNhatBoLoc(); } });
        chipMagazine.setOnCheckedChangeListener((b, c) -> { if (c) { loaiDangChon = LoaiTaiLieu.MAGAZINE; capNhatBoLoc(); } });
        chipEbook.setOnCheckedChangeListener((b, c) -> { if (c) { loaiDangChon = LoaiTaiLieu.EBOOK; capNhatBoLoc(); } });

        chipSort.setOnCheckedChangeListener((b, c) -> {
            if (c) chipPhiCaoNhat.setChecked(false);
            capNhatBoLoc();
        });
        chipPhiCaoNhat.setOnCheckedChangeListener((b, c) -> {
            if (c) chipSort.setChecked(false);
            capNhatBoLoc();
        });

        btnFirst.setOnClickListener(v -> goToPage(0));
        btnPrev.setOnClickListener(v -> goToPage(currentPage - 1));
        btnNext.setOnClickListener(v -> goToPage(currentPage + 1));
        btnLast.setOnClickListener(v -> goToPage(tongSoTrang() - 1));

        capNhatBoLoc();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capNhatBoLoc();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_them) {
            startActivity(new Intent(this, EditActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void capNhatBoLoc() {
        List<Document> ds = loaiDangChon != null ? repo.locTheoLoai(loaiDangChon) : repo.getAll();

        String tk = etSearch.getText() == null ? "" : etSearch.getText().toString().trim();
        if (!tk.isEmpty()) {
            String tkLower = tk.toLowerCase(Locale.ROOT);
            List<Document> tmp = new ArrayList<>();
            for (Document d : ds) {
                if (d.getTenTaiLieu().toLowerCase(Locale.ROOT).contains(tkLower)) tmp.add(d);
            }
            ds = tmp;
        }

        if (chipPhiCaoNhat.isChecked()) {
            Document max = null;
            for (Document d : ds) {
                if (max == null || d.tinhPhiMuon() > max.tinhPhiMuon()) max = d;
            }
            filteredList = max == null ? new ArrayList<>() : Collections.singletonList(max);
        } else {
            if (chipSort.isChecked()) {
                Collections.sort(ds, (a, b) -> Double.compare(b.tinhPhiMuon(), a.tinhPhiMuon()));
            }
            filteredList = ds;
        }
        currentPage = 0;
        renderPage();
    }

    private void goToPage(int p) {
        int total = tongSoTrang();
        if (p < 0) p = 0;
        if (p > total - 1) p = total - 1;
        currentPage = p;
        renderPage();
    }

    private int tongSoTrang() {
        return Math.max(1, (int) Math.ceil(filteredList.size() / (double) PAGE_SIZE));
    }

    private void renderPage() {
        int total = filteredList.size();
        int totalPages = tongSoTrang();
        if (currentPage >= totalPages) currentPage = totalPages - 1;
        if (currentPage < 0) currentPage = 0;

        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, total);
        List<Document> page = total == 0 ? new ArrayList<>() : new ArrayList<>(filteredList.subList(start, end));
        adapter.setData(page);

        tvEmpty.setVisibility(total == 0 ? View.VISIBLE : View.GONE);
        paginationBar.setVisibility(total > PAGE_SIZE ? View.VISIBLE : View.GONE);

        renderPagination(totalPages);
    }

    private void renderPagination(int totalPages) {
        btnFirst.setEnabled(currentPage > 0);
        btnPrev.setEnabled(currentPage > 0);
        btnNext.setEnabled(currentPage < totalPages - 1);
        btnLast.setEnabled(currentPage < totalPages - 1);
        btnFirst.setAlpha(btnFirst.isEnabled() ? 1f : 0.35f);
        btnPrev.setAlpha(btnPrev.isEnabled() ? 1f : 0.35f);
        btnNext.setAlpha(btnNext.isEnabled() ? 1f : 0.35f);
        btnLast.setAlpha(btnLast.isEnabled() ? 1f : 0.35f);

        pageNumbersContainer.removeAllViews();
        int[] pages = visiblePages(currentPage, totalPages);
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int p : pages) {
            if (p == -1) {
                View dots = inflater.inflate(R.layout.item_page_ellipsis, pageNumbersContainer, false);
                pageNumbersContainer.addView(dots);
            } else {
                int layoutId = (p == currentPage) ? R.layout.item_page_active : R.layout.item_page_inactive;
                MaterialButton btn = (MaterialButton) inflater.inflate(layoutId, pageNumbersContainer, false);
                btn.setText(String.valueOf(p + 1));
                final int target = p;
                btn.setOnClickListener(v -> goToPage(target));
                pageNumbersContainer.addView(btn);
            }
        }
    }

    /** Trả về dãy số trang để hiển thị; -1 = ellipsis. Window quanh currentPage. */
    private int[] visiblePages(int current, int total) {
        if (total <= 7) {
            int[] arr = new int[total];
            for (int i = 0; i < total; i++) arr[i] = i;
            return arr;
        }
        List<Integer> list = new ArrayList<>();
        list.add(0);
        if (current > 2) list.add(-1);
        for (int i = current - 1; i <= current + 1; i++) {
            if (i > 0 && i < total - 1) list.add(i);
        }
        if (current < total - 3) list.add(-1);
        list.add(total - 1);
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }

    private void xacNhanXoa(Document d) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null);
        ImageView ivBadge = view.findViewById(R.id.ivBadge);
        TextView tvMa = view.findViewById(R.id.tvMa);
        TextView tvTen = view.findViewById(R.id.tvTen);
        MaterialButton btnHuy = view.findViewById(R.id.btnHuy);
        MaterialButton btnXoa = view.findViewById(R.id.btnXoa);

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
        tvMa.setText("Mã: " + d.getMaTaiLieu());
        tvTen.setText(d.getTenTaiLieu());

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(view)
                .setBackgroundInsetTop(0)
                .setBackgroundInsetBottom(0)
                .create();

        btnHuy.setOnClickListener(v -> dialog.dismiss());
        btnXoa.setOnClickListener(v -> {
            repo.xoa(d.getMaTaiLieu());
            capNhatBoLoc();
            Toast.makeText(this, "Đã xoá", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}
