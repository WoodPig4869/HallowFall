package tw.liangze.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.liangze.backend.model.Comment;
import tw.liangze.backend.model.CommentDTO;
import tw.liangze.backend.repository.CommentRepository;
import tw.liangze.backend.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 根據 postId 取得該文章的所有留言
     * @param postId
     * @return List<CommentDTO> 包含 留言者id,留言者名稱,留言內容,留言時間 的 List
     */
    @GetMapping("/{postId}")
    public List<CommentDTO> getCommentsByPostId(@PathVariable("postId") int postId) {
        return commentService.getByPostId(postId);
    }

    /**
     * 新增留言
     * @param comment 物件，內容包含 postId, content
     * @return 成功回傳 狀態碼 201，失敗回傳 狀態碼 400
     */
    @PostMapping("")
    public ResponseEntity<String> addComment(@RequestBody Comment comment) {
        if (commentService.addComment(comment)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("留言新增成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("留言新增失敗");
        }
    }

}
