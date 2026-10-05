package vn.edu.ueh.thanhdnh.firebase_example;

import com.google.firebase.firestore.Exclude;
import java.io.Serializable;

public class Article implements Serializable {
  private String docId; // Chỉ lưu trong RAM của máy, không đẩy lên Firestore
  private String id;    // ID thủ công do bạn nhập (sẽ lưu lên Firestore)
  private String title;
  private String content;
  private String img_cover;
  private int view;

  public Article() {}

  public Article(String id, String title, String content, String img_cover, int view) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.img_cover = img_cover;
    this.view = view;
  }

  public Article(String docId, String id, String title, String content, String img_cover, int view) {
    this.docId = docId;
    this.id = id;
    this.title = title;
    this.content = content;
    this.img_cover = img_cover;
    this.view = view;
  }

  // Đánh dấu @Exclude để Firebase bỏ qua docId, không tạo trường này trên Firestore
  @Exclude
  public String getDocId() {
    return docId;
  }

  public void setDocId(String docId) {
    this.docId = docId;
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getContent() { return content; }
  public void setContent(String content) { this.content = content; }

  public String getImg_cover() { return img_cover; }
  public void setImg_cover(String img_cover) { this.img_cover = img_cover; }

  public int getView() { return view; }
  public void setView(int view) { this.view = view; }
}