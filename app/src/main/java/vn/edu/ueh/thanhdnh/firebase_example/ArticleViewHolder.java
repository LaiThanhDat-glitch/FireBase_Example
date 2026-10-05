package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
  private TextView txtTitle, txtContent, txtView;
  private ImageView imgCover;
  private ArticleViewAdapter adapter;

  public ArticleViewHolder(@NonNull View itemView, ArticleViewAdapter adapter) {
    super(itemView);
    this.adapter = adapter;

    txtTitle = itemView.findViewById(R.id.txt_title);
    txtContent = itemView.findViewById(R.id.txt_content);
    txtView = itemView.findViewById(R.id.txt_view);
    imgCover = itemView.findViewById(R.id.img_cover);

    // Bắt sự kiện Click vào 1 dòng bài viết
    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION && adapter != null) {
          Article clickedArticle = adapter.getArticles().get(position);
          Context context = v.getContext();

          // 1. Tăng view lên +1 trực tiếp trên Firestore (atomic increment)
          if (clickedArticle.getDocId() != null) {
            FirebaseFirestore.getInstance()
                    .collection("articles")
                    .document(clickedArticle.getDocId()) // Trỏ đúng vào DocID ngẫu nhiên của Firestore
                    .update("view", FieldValue.increment(1));
          }

          // 2. Chuyển sang màn hình xem chi tiết
          Intent intent = new Intent(context, DetailActivity.class);
          // Cập nhật lượt xem cục bộ để màn hình chi tiết nhận ngay số mới (+1)
          clickedArticle.setView(clickedArticle.getView() + 1);
          intent.putExtra("article_item", clickedArticle);
          context.startActivity(intent);
        }
      }
    });
  }

  public TextView getTxtTitle() { return txtTitle; }
  public TextView getTxtContent() { return txtContent; }
  public TextView getTxtView() { return txtView; }
  public ImageView getImgCover() { return imgCover; }
}