package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArticleViewAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
  private LayoutInflater mInflater;
  private List<Article> articles;
  private Context context;

  public ArticleViewAdapter(Context context, List<Article> articles) {
    this.context = context;
    this.mInflater = LayoutInflater.from(context);
    this.articles = articles;
  }

  public void update(List<Article> articles) {
    this.articles = articles;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Đổi R.layout.contact_list thành file layout item bài viết của bạn (ví dụ: R.layout.article_item hoặc R.layout.contact_list nếu chưa đổi tên layout)
    View customView = mInflater.inflate(R.layout.article_item, parent, false);
    return new ArticleViewHolder(customView, this);
  }

  @Override
  public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
    Article currentArticle = articles.get(position);

    // Gán dữ liệu theo các thuộc tính trên ảnh: title, content, views
    holder.getTxtTitle().setText(currentArticle.getTitle());
    holder.getTxtContent().setText(currentArticle.getContent());
    holder.getTxtView().setText("Views: " + currentArticle.getView());

    // Xử lý hiển thị img_cover
    String imgCover = currentArticle.getImg_cover();
    if (imgCover != null && !imgCover.isEmpty()) {
      // Trường hợp ảnh lưu dưới dạng Base64
      try {
        byte[] decodedString = Base64.decode(imgCover, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.getImgCover().setImageBitmap(decodedByte);
      } catch (Exception e) {
        // Nếu bạn dùng thư viện tải URL như Glide hay Picasso:
        // Glide.with(context).load(imgCover).into(holder.getImgCover());
      }
    }
  }

  @Override
  public int getItemCount() {
    return articles != null ? articles.size() : 0;
  }

  public List<Article> getArticles() {
    return articles;
  }
}