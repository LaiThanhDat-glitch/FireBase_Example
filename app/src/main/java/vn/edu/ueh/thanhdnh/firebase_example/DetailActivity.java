package vn.edu.ueh.thanhdnh.firebase_example;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class DetailActivity extends AppCompatActivity {
    private TextView txtTitle, txtContent, txtView;
    private ImageView imgCover;
    private Button btnBack;

    private FirebaseFirestore db;
    private ListenerRegistration articleListener; // Quản lý listener để tránh rò rỉ bộ nhớ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtTitle = findViewById(R.id.txtDetailTitle);
        txtContent = findViewById(R.id.txtDetailContent);
        txtView = findViewById(R.id.txtDetailView);
        imgCover = findViewById(R.id.imgDetailCover);
        btnBack = findViewById(R.id.btnBackDetail);

        btnBack.setOnClickListener(v -> finish());

        db = FirebaseFirestore.getInstance();

        // Nhận dữ liệu truyền từ RecyclerView
        Article initialArticle;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            initialArticle = getIntent().getSerializableExtra("article_item", Article.class);
        } else {
            initialArticle = (Article) getIntent().getSerializableExtra("article_item");
        }

        if (initialArticle != null) {
            // 1. Hiển thị dữ liệu tạm thời ngay khi mở màn hình
            displayData(initialArticle.getTitle(), initialArticle.getContent(),
                    initialArticle.getImg_cover(), initialArticle.getView());

            // 2. Lắng nghe Realtime đúng Document này trên Firestore qua docId
            String docId = initialArticle.getDocId();
            if (docId != null && !docId.isEmpty()) {
                DocumentReference docRef = db.collection("articles").document(docId);
                articleListener = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            // Nhận dữ liệu cập nhật mới nhất từ Firestore
                            String title = snapshot.getString("title");
                            String content = snapshot.getString("content");
                            String imgCoverName = snapshot.getString("img_cover");

                            Long viewCount = snapshot.getLong("view");
                            int view = viewCount != null ? viewCount.intValue() : 0;

                            // Cập nhật lên giao diện ngay lập tức
                            displayData(title, content, imgCoverName, view);
                        }
                    }
                });
            }
        }
    }

    // Hàm phụ trợ gán dữ liệu lên giao diện
    private void displayData(String title, String content, String imgCoverName, int view) {
        txtTitle.setText(title != null ? title : "");
        txtContent.setText(content != null ? content : "");
        txtView.setText("Views: " + view);

        if (imgCoverName != null && !imgCoverName.trim().isEmpty()) {
            int resId = getResources().getIdentifier(
                    imgCoverName.trim().toLowerCase(),
                    "drawable",
                    getPackageName()
            );
            if (resId != 0) {
                imgCover.setImageResource(resId);
            } else {
                imgCover.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            imgCover.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy lắng nghe khi thoát màn hình để giải phóng RAM
        if (articleListener != null) {
            articleListener.remove();
        }
    }
}