package com.example.ktgk.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ktgk.R;
import com.example.ktgk.model.Document;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.VH> {

    public interface Listener {
        void onClick(Document d);
        void onSua(Document d);
        void onXoa(Document d);
    }

    private static final NumberFormat MONEY_FORMAT = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    private final List<Document> data = new ArrayList<>();
    private final Listener listener;

    public DocumentAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setData(List<Document> moi) {
        data.clear();
        if (moi != null) data.addAll(moi);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Document d = data.get(position);
        h.tvTen.setText(d.getTenTaiLieu());
        h.tvMa.setText("Mã: " + d.getMaTaiLieu());
        h.tvPhi.setText("Phí mượn: " + MONEY_FORMAT.format(d.tinhPhiMuon()) + " ₫");

        switch (d.getLoai()) {
            case BOOK:
                h.ivBadge.setBackgroundResource(R.drawable.bg_badge_book);
                h.ivBadge.setImageResource(R.drawable.ic_book_24);
                break;
            case MAGAZINE:
                h.ivBadge.setBackgroundResource(R.drawable.bg_badge_magazine);
                h.ivBadge.setImageResource(R.drawable.ic_magazine_24);
                break;
            case EBOOK:
                h.ivBadge.setBackgroundResource(R.drawable.bg_badge_ebook);
                h.ivBadge.setImageResource(R.drawable.ic_ebook_24);
                break;
        }

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(d);
        });
        h.btnSua.setOnClickListener(v -> {
            if (listener != null) listener.onSua(d);
        });
        h.btnXoa.setOnClickListener(v -> {
            if (listener != null) listener.onXoa(d);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivBadge;
        TextView tvTen, tvMa, tvPhi;
        ImageButton btnSua, btnXoa;

        VH(View v) {
            super(v);
            ivBadge = v.findViewById(R.id.ivBadge);
            tvTen = v.findViewById(R.id.tvTen);
            tvMa = v.findViewById(R.id.tvMa);
            tvPhi = v.findViewById(R.id.tvPhi);
            btnSua = v.findViewById(R.id.btnSua);
            btnXoa = v.findViewById(R.id.btnXoa);
        }
    }
}
