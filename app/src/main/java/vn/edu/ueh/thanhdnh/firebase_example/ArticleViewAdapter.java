package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Context;
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
    View customView = mInflater.inflate(R.layout.article_item, parent, false);
    return new ArticleViewHolder(customView, this);
  }

  @Override
  public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
    Article currentArticle = articles.get(position);

    holder.getTxtTitle().setText(currentArticle.getTitle());
    holder.getTxtContent().setText(currentArticle.getContent());
    holder.getTxtView().setText("Views: " + currentArticle.getView());

    // Xử lý lấy ảnh từ res/drawable bằng tên file lưu trên Firebase
    String imageName = currentArticle.getImg_cover();
    if (imageName != null && !imageName.trim().isEmpty()) {
      // Tìm resource ID dựa trên tên chuỗi (loại bỏ đuôi file và khoảng trắng)
      String cleanImageName = imageName.trim().toLowerCase();
      int resId = context.getResources().getIdentifier(cleanImageName, "drawable", context.getPackageName());

      if (resId != 0) {
        // Tìm thấy ảnh trong drawable
        holder.getImgCover().setImageResource(resId);
      } else {
        // Tên file không khớp thì hiện icon mặc định
        holder.getImgCover().setImageResource(android.R.drawable.ic_menu_gallery);
      }
    } else {
      holder.getImgCover().setImageResource(android.R.drawable.ic_menu_gallery);
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