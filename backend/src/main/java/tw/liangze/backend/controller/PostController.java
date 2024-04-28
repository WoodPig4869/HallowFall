package tw.liangze.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.liangze.backend.model.Post;
import tw.liangze.backend.model.PostDTO;
import tw.liangze.backend.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    //    根據userId取得文章列表(不包含內文)
    @GetMapping("/dto/{userId}")
    public List<PostDTO> getPosts(@PathVariable(required = false) Integer userId) {
        if (userId == null) {
            userId = 0;
        }
        return postService.find(userId);
    }

//    根據id取得文章
    @GetMapping("/{postId}")
    public Post getPost(@PathVariable int postId) {
        return postService.findById(postId);
    }

    /**
     * 新增文章
     * @param post 文章物件，內容包含id,userId,title, content,image可以為空
     * @return 成功回傳 狀態碼 201，失敗回傳 狀態碼 400
     */
    @PostMapping("")
    public ResponseEntity<String> addPost(@RequestBody Post post) {
        if (postService.add(post)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("文章新增成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文章新增失敗");
        }
    }

    @PutMapping("")
    public ResponseEntity<String> updatePost(@RequestBody Post post) {
        if (postService.update(post)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("文章更新成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文章更新失敗");
        }
    }

//    刪除文章
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable int id) {
        if (postService.delete(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("文章刪除成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文章刪除失敗");
        }
    }
}
