package com.example.ktgk.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.ktgk.model.Book;
import com.example.ktgk.model.Document;
import com.example.ktgk.model.Ebook;
import com.example.ktgk.model.LoaiTaiLieu;
import com.example.ktgk.model.Magazine;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class DocumentRepository {

    private static final String TAG = "DocumentRepository";
    private static final String FILE_NAME = "documents.json";
    private static final String PREFS_NAME = "doc_repo_prefs";
    private static final String KEY_SEED_VERSION = "seed_version";
    private static final int CURRENT_SEED_VERSION = 3;

    private static DocumentRepository instance;

    private final Context appContext;
    private final List<Document> documents = new ArrayList<>();

    private DocumentRepository(Context appContext) {
        this.appContext = appContext;
        load();
    }

    public static synchronized DocumentRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DocumentRepository(context.getApplicationContext());
        }
        return instance;
    }

    private File getFile() {
        return new File(appContext.getFilesDir(), FILE_NAME);
    }

    private void seed() {
        documents.add(new Book("B01", "Lập trình Java cơ bản", 200000, 350));
        documents.add(new Book("B02", "Cấu trúc dữ liệu & Giải thuật", 180000, 420));
        documents.add(new Book("B03", "Toán rời rạc", 150000, 300));
        documents.add(new Book("B04", "Mạng máy tính", 220000, 480));
        documents.add(new Book("B05", "Hệ điều hành Linux", 250000, 550));
        documents.add(new Book("B06", "Trí tuệ nhân tạo", 320000, 620));
        documents.add(new Book("B07", "Kỹ thuật phần mềm", 280000, 510));
        documents.add(new Book("B08", "Cơ sở dữ liệu", 200000, 380));
        documents.add(new Book("B09", "Lập trình Python", 220000, 400));
        documents.add(new Book("B10", "Lập trình C++", 250000, 480));
        documents.add(new Book("B11", "Thiết kế Web", 200000, 360));
        documents.add(new Book("B12", "An toàn thông tin", 280000, 540));
        documents.add(new Book("B13", "Phân tích & Thiết kế hệ thống", 260000, 520));
        documents.add(new Book("B14", "Kiểm thử phần mềm", 240000, 460));
        documents.add(new Book("B15", "Lập trình hướng đối tượng", 230000, 420));
        documents.add(new Book("B16", "Đại số tuyến tính", 180000, 380));
        documents.add(new Book("B17", "Giải tích 1", 170000, 350));
        documents.add(new Book("B18", "Giải tích 2", 175000, 360));
        documents.add(new Book("B19", "Xác suất thống kê", 200000, 400));
        documents.add(new Book("B20", "Vật lý đại cương", 190000, 480));
        documents.add(new Book("B21", "Tiếng Anh chuyên ngành CNTT", 160000, 280));
        documents.add(new Book("B22", "Compiler Construction", 350000, 700));
        documents.add(new Book("B23", "Computer Graphics", 320000, 580));
        documents.add(new Book("B24", "Machine Learning cơ bản", 380000, 650));
        documents.add(new Book("B25", "Deep Learning", 400000, 720));
        documents.add(new Book("B26", "Big Data Analytics", 360000, 600));
        documents.add(new Book("B27", "Blockchain căn bản", 290000, 480));
        documents.add(new Book("B28", "IoT thực hành", 270000, 440));

        documents.add(new Magazine("M01", "Tạp chí IT Today", 50000, 12));
        documents.add(new Magazine("M02", "Khoa học & Đời sống", 45000, 8));
        documents.add(new Magazine("M03", "Echip Mobile", 35000, 24));
        documents.add(new Magazine("M04", "PC World VN", 60000, 6));
        documents.add(new Magazine("M05", "Robohub Vietnam", 55000, 10));
        documents.add(new Magazine("M06", "Make Magazine", 70000, 4));
        documents.add(new Magazine("M07", "Tia Sáng", 40000, 16));
        documents.add(new Magazine("M08", "Khoa học Phổ thông", 38000, 20));
        documents.add(new Magazine("M09", "Wired Vietnam", 80000, 8));
        documents.add(new Magazine("M10", "Forbes Vietnam Tech", 90000, 12));
        documents.add(new Magazine("M11", "Hardware Magazine", 65000, 6));
        documents.add(new Magazine("M12", "Tạp chí Toán học", 50000, 4));
        documents.add(new Magazine("M13", "Game Developer Mag", 75000, 10));
        documents.add(new Magazine("M14", "UX Design Quarterly", 70000, 4));
        documents.add(new Magazine("M15", "Cyber Security Today", 85000, 12));
        documents.add(new Magazine("M16", "Cloud Native Mag", 90000, 6));
        documents.add(new Magazine("M17", "DevOps Weekly", 60000, 52));
        documents.add(new Magazine("M18", "Open Source Today", 55000, 12));
        documents.add(new Magazine("M19", "Mobile Dev Trends", 65000, 8));
        documents.add(new Magazine("M20", "AI Frontier", 100000, 6));
        documents.add(new Magazine("M21", "Data Science Hub", 95000, 10));

        documents.add(new Ebook("E01", "Android cơ bản", 150000, 2.5));
        documents.add(new Ebook("E02", "Kotlin nâng cao", 180000, 3.2));
        documents.add(new Ebook("E03", "React Native từ A-Z", 200000, 4.8));
        documents.add(new Ebook("E04", "Docker & Kubernetes", 220000, 5.5));
        documents.add(new Ebook("E05", "Clean Code", 160000, 1.8));
        documents.add(new Ebook("E06", "Design Patterns", 190000, 3.0));
        documents.add(new Ebook("E07", "SQL nâng cao", 170000, 2.2));
        documents.add(new Ebook("E08", "NoSQL với MongoDB", 180000, 2.8));
        documents.add(new Ebook("E09", "Microservices", 220000, 4.0));
        documents.add(new Ebook("E10", "GraphQL Mastery", 200000, 3.5));
        documents.add(new Ebook("E11", "Vue.js từ A-Z", 190000, 4.2));
        documents.add(new Ebook("E12", "Angular nâng cao", 210000, 4.8));
        documents.add(new Ebook("E13", "Node.js Production", 230000, 5.0));
        documents.add(new Ebook("E14", "Spring Boot", 240000, 5.5));
        documents.add(new Ebook("E15", "Flutter cho Android", 200000, 4.5));
        documents.add(new Ebook("E16", "SwiftUI căn bản", 180000, 3.8));
        documents.add(new Ebook("E17", "Rust thực chiến", 250000, 6.2));
        documents.add(new Ebook("E18", "Go Programming", 220000, 5.0));
        documents.add(new Ebook("E19", "TypeScript Pro", 200000, 4.0));
        documents.add(new Ebook("E20", "GraphQL với Apollo", 210000, 3.6));
        documents.add(new Ebook("E21", "Linux Bash Shell", 150000, 1.5));
    }

    private void load() {
        documents.clear();
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedVer = prefs.getInt(KEY_SEED_VERSION, 0);
        File file = getFile();
        if (!file.exists() || savedVer < CURRENT_SEED_VERSION) {
            seed();
            save();
            prefs.edit().putInt(KEY_SEED_VERSION, CURRENT_SEED_VERSION).apply();
            return;
        }
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) sb.append(line);
            if (sb.length() == 0) {
                seed();
                save();
                return;
            }
            JSONArray arr = new JSONArray(sb.toString());
            for (int i = 0; i < arr.length(); i++) {
                Document d = fromJson(arr.getJSONObject(i));
                if (d != null) documents.add(d);
            }
        } catch (Exception e) {
            Log.e(TAG, "load failed", e);
            documents.clear();
            seed();
            save();
        }
    }

    private void save() {
        File file = getFile();
        JSONArray arr = new JSONArray();
        try {
            for (Document d : documents) {
                arr.put(toJson(d));
            }
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(arr.toString().getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            Log.e(TAG, "save failed", e);
        }
    }

    private JSONObject toJson(Document d) throws Exception {
        JSONObject o = new JSONObject();
        o.put("loai", d.getLoai().name());
        o.put("ma", d.getMaTaiLieu());
        o.put("ten", d.getTenTaiLieu());
        o.put("gia", d.getGiaTien());
        if (d instanceof Book) {
            o.put("soTrang", ((Book) d).getSoTrang());
        } else if (d instanceof Magazine) {
            o.put("soKy", ((Magazine) d).getSoKyPhatHanh());
        } else if (d instanceof Ebook) {
            o.put("dungLuong", ((Ebook) d).getDungLuongFile());
        }
        return o;
    }

    private Document fromJson(JSONObject o) {
        try {
            LoaiTaiLieu loai = LoaiTaiLieu.valueOf(o.getString("loai"));
            String ma = o.getString("ma");
            String ten = o.getString("ten");
            double gia = o.getDouble("gia");
            switch (loai) {
                case BOOK:
                    return new Book(ma, ten, gia, o.getInt("soTrang"));
                case MAGAZINE:
                    return new Magazine(ma, ten, gia, o.getInt("soKy"));
                case EBOOK:
                    return new Ebook(ma, ten, gia, o.getDouble("dungLuong"));
            }
        } catch (Exception e) {
            Log.e(TAG, "fromJson failed", e);
        }
        return null;
    }

    public List<Document> getAll() {
        return new ArrayList<>(documents);
    }

    public Document findByMa(String maTaiLieu) {
        if (maTaiLieu == null) return null;
        for (Document d : documents) {
            if (d.getMaTaiLieu().equalsIgnoreCase(maTaiLieu)) {
                return d;
            }
        }
        return null;
    }

    public void them(Document document) {
        if (findByMa(document.getMaTaiLieu()) != null) {
            throw new IllegalArgumentException("Mã tài liệu đã tồn tại");
        }
        documents.add(document);
        save();
    }

    public void capNhat(String maCu, Document moi) {
        for (int i = 0; i < documents.size(); i++) {
            if (documents.get(i).getMaTaiLieu().equalsIgnoreCase(maCu)) {
                if (!maCu.equalsIgnoreCase(moi.getMaTaiLieu())
                        && findByMa(moi.getMaTaiLieu()) != null) {
                    throw new IllegalArgumentException("Mã tài liệu đã tồn tại");
                }
                documents.set(i, moi);
                save();
                return;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy tài liệu cần cập nhật");
    }

    public void xoa(String maTaiLieu) {
        boolean removed = documents.removeIf(d -> d.getMaTaiLieu().equalsIgnoreCase(maTaiLieu));
        if (removed) save();
    }

    public List<Document> timTheoTen(String tuKhoa) {
        List<Document> ket_qua = new ArrayList<>();
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return getAll();
        }
        String tk = tuKhoa.toLowerCase(Locale.ROOT).trim();
        for (Document d : documents) {
            if (d.getTenTaiLieu().toLowerCase(Locale.ROOT).contains(tk)) {
                ket_qua.add(d);
            }
        }
        return ket_qua;
    }

    public List<Document> locTheoLoai(LoaiTaiLieu loai) {
        if (loai == null) return getAll();
        List<Document> ket_qua = new ArrayList<>();
        for (Document d : documents) {
            if (d.getLoai() == loai) {
                ket_qua.add(d);
            }
        }
        return ket_qua;
    }

    public List<Document> sapXepPhiMuonGiamDan() {
        List<Document> ds = getAll();
        Collections.sort(ds, new Comparator<Document>() {
            @Override
            public int compare(Document a, Document b) {
                return Double.compare(b.tinhPhiMuon(), a.tinhPhiMuon());
            }
        });
        return ds;
    }

    public Document phiMuonCaoNhat() {
        Document max = null;
        for (Document d : documents) {
            if (max == null || d.tinhPhiMuon() > max.tinhPhiMuon()) {
                max = d;
            }
        }
        return max;
    }
}
