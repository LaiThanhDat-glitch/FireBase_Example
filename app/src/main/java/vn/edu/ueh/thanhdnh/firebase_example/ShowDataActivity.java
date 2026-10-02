package vn.edu.ueh.thanhdnh.firebase_example;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowDataActivity extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView recyclerView;
    List<Article> articles = new ArrayList<>();
    ArticleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);

        recyclerView = findViewById(R.id.reclyclerview);
        adapter = new ArticleViewAdapter(this, articles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Lắng nghe dữ liệu thời gian thực từ collection "articles"
        db.collection("articles").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }

                if (snapshots != null) {
                    articles.clear();
                    for (QueryDocumentSnapshot q : snapshots) {
                        Map<String, Object> data = q.getData();

                        String docId = q.getId(); // Lấy ID document
                        String title = data.get("title") != null ? data.get("title").toString() : "";
                        String content = data.get("content") != null ? data.get("content").toString() : "";
                        String imgCover = data.get("img_cover") != null ? data.get("img_cover").toString() : "";

                        int view = 0;
                        if (data.get("view") != null) {
                            view = ((Number) data.get("view")).intValue();
                        }

                        Article article = new Article(docId, title, content, imgCover, view);
                        articles.add(article);
                    }
                    adapter.update(articles);
                }
            }
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity hiện tại và quay về màn hình trước
            }
        });
    }
}