package tw.liangze.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import tw.liangze.backend.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPostIdOrderByCreatedAtDesc(int postId);

    /**
     * 新增留言
     * @param postId
     * @param userId
     * @param content
     */
    @Procedure(name = "insertComment")
    public void insertComment(int postId, int userId, String content);
}
