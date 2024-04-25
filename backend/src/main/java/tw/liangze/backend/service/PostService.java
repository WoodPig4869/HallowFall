package tw.liangze.backend.service;


import org.springframework.stereotype.Service;
import tw.liangze.backend.model.Post;
import tw.liangze.backend.model.PostDTO;
import tw.liangze.backend.repository.PostRepository;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }


    /**
     * 根據 userId 查詢文章列表(不包含內文)，輸入 0 查詢所有文章。
     * @param userId 使用者 ID。
     * @return 符合條件的文章列表。
     */
    public List<PostDTO> find(int userId) {
//        三元運算子，如果 userId 為 0 查詢所有文章，否則根據userId查詢文章。
        List<Post> posts = (userId == 0)?
                postRepository.findByDeletedFalseOrderByCreatedAtDesc():
                postRepository.findByUserIdAndDeletedFalseOrderByCreatedAtDesc(userId);

        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            PostDTO postDTO = new PostDTO();
            postDTO.setPostId(post.getPostId());
            postDTO.setTitle(post.getTitle());
            postDTO.setUserId(post.getUserId());
            String nickname = userService.getNicknameById(post.getUserId());
            postDTO.setNickname(nickname);
            postDTO.setImage(post.getImage());
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setUpdatedAt(post.getUpdatedAt());
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }

    public Post findById(int id) {
        return postRepository.findByPostIdAndDeletedFalse(id);
    }

    /**
     * 新增文章
     * @param post(userId, title, content, image) 文章資料，image 為可選。
     * @return boolean 新增成功返回 true，失敗返回 false。
     */
    public boolean add(Post post) {
        // 設定當前登入的用戶 id
        post.setUserId(userService.getSelfId());
        // 如果沒有設定圖片，就設定 Image 為空字串
        if(post.getImage() == null){
            post.setImage("");
        }
        try {
            postRepository.insertPost(
                    post.getUserId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getImage()
            );
        }catch (Exception e) {
            System.err.println("新增文章失敗，格式錯誤：" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    return true;
    }

    /**
     * 刪除文章
     * @param id 文章 ID。
     * @return boolean 刪除成功返回 true，失敗返回 false。
     */
    public boolean delete(int id) {
        try {
            Post post = postRepository.findById(id).orElseThrow();
            // 找到當前用戶id
            int targetUserId = userService.getSelfId();
            // 找到當前用戶身份
            String role = userService.getSelfRole();
            // 確認是否為自己的文章，或是管理員身份
            if (post.getUserId() == targetUserId|| role.equals("ADMIN")|| role.equals("ROOT")) {
                // 設定文章為已刪除
                post.setDeleted(true);
                postRepository.save(post);
                return true;
            }
            return false;
        }
        catch (Exception e) {
            System.err.println("刪除文章失敗：" + e.getMessage());}
            return false;
    }

    /**
     * 更新文章
     * @param post(title, content, image) 文章資料，全部欄位皆為可選。
     * @return boolean 更新成功返回 true，失敗返回 false。
     */
    public boolean update(Post post) {
        try {
            // 找到當前用戶id
            int targetUserId = userService.getSelfId();
            // 找到當前用戶身份
            String role = userService.getSelfRole();
            // 找到要更新的文章
            Post targetPost =  postRepository.findByPostIdAndDeletedFalse(post.getPostId());
            // 確認是否為自己的文章，或是ROOT身份
            int targetPostUserId = targetPost.getUserId();
            if (targetPostUserId == targetUserId|| role.equals("ROOT")) {
                System.out.println(targetPost.getUserId());
                if (post.getTitle() != null) {
                    targetPost.setTitle(post.getTitle());
                }
                if (post.getContent() != null) {
                    targetPost.setContent(post.getContent());
                }
                if (post.getImage() != null) {
                    targetPost.setImage(post.getImage());
                }
                // 設定最後更新時間
                targetPost.setUpdatedAt(new Date());
                postRepository.save(targetPost);
                return true;
            }
            return false;
        }
        catch (Exception e) {
            System.err.println("更新文章失敗：" + e.getMessage());}
            return false;
    }

}
