package tw.liangze.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.liangze.backend.model.Comment;
import tw.liangze.backend.model.CommentDTO;
import tw.liangze.backend.repository.CommentRepository;
import tw.liangze.backend.service.CommentService;

import java.util.List;

@Tag(name = "留言控制", description = "管理Comment物件")
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "查詢留言", description = "根據postId查詢留言")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "回傳留言列表"),
            @ApiResponse(responseCode = "404", description = "查無資料")
    })
    @GetMapping("/{postId}")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable("postId") int postId) {
        List<CommentDTO> result = commentService.getByPostId(postId);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @Operation(summary = "新增留言", description = "新增留言，留言者為當前登入會員")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "留言新增成功"),
            @ApiResponse(responseCode = "400", description = "留言新增失敗")
    })
    @PostMapping("")
    public ResponseEntity<String> addComment(@RequestBody Comment comment) {
        if (commentService.addComment(comment)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("留言新增成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("留言新增失敗");
        }
    }

}
