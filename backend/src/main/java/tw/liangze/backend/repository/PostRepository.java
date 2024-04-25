package tw.liangze.backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import tw.liangze.backend.model.Post;
import tw.liangze.backend.model.PostDTO;

import java.util.List;

public interface PostRepository extends JpaRepository <Post, Integer> {
    // 查詢所有文章，隨著時間降序排序
    public List<Post> findByDeletedFalseOrderByCreatedAtDesc();
    // 根據發文者ID查詢文章，隨著時間降序排序
    public List<Post> findByUserIdAndDeletedFalseOrderByCreatedAtDesc(int userId);

    // 根據文章ID查詢文章
    public Post findByPostIdAndDeletedFalse(int postId);


    // 新增文章，傳入( Title, Content, Image)
    @Procedure(name = "insertPost")
    public void insertPost(int userId, String title, String content, String image);

}
