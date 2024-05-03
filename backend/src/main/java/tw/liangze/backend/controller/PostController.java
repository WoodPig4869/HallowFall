package tw.liangze.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.liangze.backend.model.Post;
import tw.liangze.backend.model.PostDTO;
import tw.liangze.backend.service.PostService;

import java.util.List;

@Tag(name = "文章控制", description = "管理Post物件")
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @Operation(summary = "查詢文章列表", description = "根據userId查詢文章列表，userId為0表示所有文章")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "回傳文章列表"),
            @ApiResponse(responseCode = "404", description = "查無資料")
    })
    @GetMapping("/dto/{userId}")
    public ResponseEntity<?> getPosts(@PathVariable(required = false) Integer userId) {
        List<PostDTO> result = postService.find(userId);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @Operation(summary = "查詢文章內容", description = "根據postId查詢文章內容")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "回傳文章內容"),
            @ApiResponse(responseCode = "404", description = "查無資料")
    })
    @GetMapping("/{postId}")
    public Post getPost(@PathVariable int postId) {
        return postService.findById(postId);
    }

    @Operation(summary = "新增文章", description = "傳入Post物件新增文章")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "文章新增成功"),
            @ApiResponse(responseCode = "400", description = "文章新增失敗")
    })
    @PostMapping("")
    public ResponseEntity<String> addPost(@RequestBody Post post) {
        if (postService.add(post)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("文章新增成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文章新增失敗");
        }
    }
    @Operation(summary = "更新文章", description = "傳入Post物件新增文章")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "文章更新成功"),
            @ApiResponse(responseCode = "400", description = "文章更新失敗")
    })
    @PutMapping("")
    public ResponseEntity<String> updatePost(@RequestBody Post post) {
        if (postService.update(post)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("文章更新成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文章更新失敗");
        }
    }

    @Operation(summary = "刪除文章", description = "根據postId刪除文章")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "文章刪除成功"),
            @ApiResponse(responseCode = "400", description = "文章刪除失敗")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable int id) {
        if (postService.delete(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("文章刪除成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文章刪除失敗");
        }
    }
}
