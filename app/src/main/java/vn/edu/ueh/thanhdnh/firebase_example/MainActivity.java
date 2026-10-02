package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  FirebaseFirestore db;
  Button btAdd, btShow;
  EditText etTitle, etContent, etImgCover;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    FirebaseApp.initializeApp(this);
    db = FirebaseFirestore.getInstance();

    btAdd = findViewById(R.id.btAdd);
    btShow = findViewById(R.id.btShow);

    // Ánh xạ lại các ô nhập liệu cho Article (chỉnh lại ID nếu layout XML đặt tên khác)
    etTitle = findViewById(R.id.etTitle);
    etContent = findViewById(R.id.etContent);
    etImgCover = findViewById(R.id.etImgCover);

    btAdd.setOnClickListener(this);
    btShow.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.btAdd) {
      String title = etTitle.getText().toString().trim();
      String content = etContent.getText().toString().trim();
      String imgCover = etImgCover.getText().toString().trim();
      int initialView = 0; // Lượt xem ban đầu là 0

      if (title.isEmpty()) {
        Toast.makeText(this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
        return;
      }

      // Đẩy object Article lên collection "articles" trên Firestore
      Article newArticle = new Article(title, content, imgCover, initialView);
      db.collection("articles").add(newArticle)
              .addOnSuccessListener(documentReference -> {
                Toast.makeText(MainActivity.this, "Thêm bài viết thành công!", Toast.LENGTH_SHORT).show();
                etTitle.setText("");
                etContent.setText("");
                etImgCover.setText("");
              })
              .addOnFailureListener(e -> {
                Toast.makeText(MainActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
              });

    } else if (view.getId() == R.id.btShow) {
      Intent intent = new Intent(getBaseContext(), ShowDataActivity.class);
      startActivity(intent);
    }
  }
}