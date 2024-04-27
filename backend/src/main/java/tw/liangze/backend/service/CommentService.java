package tw.liangze.backend.service;

import org.springframework.stereotype.Service;
import tw.liangze.backend.model.Comment;
import tw.liangze.backend.model.CommentDTO;
import tw.liangze.backend.model.User;
import tw.liangze.backend.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    /**
     * 根據文章ID，查詢該文章的所有留言
     * @param postId 文章ID
     * @return List<CommentDTO> 帶有  commentId, postId,nickname, userId, content, createdAt 的列表
     */
    public List<CommentDTO> getByPostId(int postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            User user = userService.findById(comment.getUserId());
            commentDTO.setCommentId(comment.getCommentId());
            commentDTO.setNickname(user.getNickname());
            commentDTO.setAvatar(user.getAvatar());
            commentDTO.setUserId(comment.getUserId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setCreatedAt(comment.getCreatedAt());
            commentDTOs.add(commentDTO);
        }
        return commentDTOs;
    }

    /**
     * 新增留言
     * @param comment 留言物件
     * @return Comment 新增的留言物件
     */
    public boolean addComment(Comment comment) {
        String content = comment.getContent();
        Integer userId = userService.getSelfId();
        int postId = comment.getPostId();
        if (content == null || content.isEmpty() || userId == null || postId == 0) {
            return false;
        }
        commentRepository.insertComment(postId, userId, content);
        return true;
    }
}
