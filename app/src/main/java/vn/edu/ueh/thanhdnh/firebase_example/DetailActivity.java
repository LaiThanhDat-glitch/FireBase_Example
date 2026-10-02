package vn.edu.ueh.thanhdnh.firebase_example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private TextView txtTitle, txtContent, txtView;
    private ImageView imgCover;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtTitle = findViewById(R.id.txtDetailTitle);
        txtContent = findViewById(R.id.txtDetailContent);
        txtView = findViewById(R.id.txtDetailView);
        imgCover = findViewById(R.id.imgDetailCover);
        btnBack = findViewById(R.id.btnBackDetail);

        // Nút Back đóng Activity
        btnBack.setOnClickListener(v -> finish());

        // Nhận dữ liệu truyền từ RecyclerView qua Intent
        Article article = (Article) getIntent().getSerializableExtra("article_item");
        if (article != null) {
            txtTitle.setText(article.getTitle());
            txtContent.setText(article.getContent());
            txtView.setText("Views: " + article.getView());

            String imageName = article.getImg_cover();
            if (imageName != null && !imageName.trim().isEmpty()) {
                int resId = getResources().getIdentifier(
                        imageName.trim().toLowerCase(),
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
    }
}