package tw.liangze.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tw.liangze.backend.model.Role;
import tw.liangze.backend.model.User;
import tw.liangze.backend.repository.UserLogRepository;
import tw.liangze.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest servletRequest;
    private final UserLogRepository userLogRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, HttpServletRequest servletRequest, UserLogRepository userLogRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.servletRequest = servletRequest;
        this.userLogRepository = userLogRepository;
    }


    /**
     * 根據 userId 找用戶(包含已刪除用戶)
     * @param userId
     * @return User
     */
    public User findById(Integer userId) {
        return userRepository.findById(userId).orElseThrow();
    }


    /**
     * 修改自己的用戶資料
     * @param user
     * @return 成功 true 失敗 false，如果有更改密碼，記錄操作 log
     */
    public boolean updateUser(User user) {
        try {
            //        找到當前用戶
            User targetUser = getSelf();
            //        更新用戶資料
            if (user.getPassword() != null) {
                String ip = servletRequest.getHeader("X-Forwarded-For");
                if (ip == null) {
                    ip = "unknown";
                }
                userLogRepository.insertUserLog(targetUser.getUserId(), ip, "修改密碼");
                targetUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getPhone() != null) {
                //        確認電話號碼是否已被使用
                if (userRepository.findByPhone(user.getPhone()).isPresent()) {
                    return false;
                }
                targetUser.setPhone(user.getPhone());
            }
            if (user.getNickname() != null)
                targetUser.setNickname(user.getNickname());
            if (user.getSignature() != null)
                targetUser.setSignature(user.getSignature());
            if (user.getAvatar() != null)
                targetUser.setAvatar(user.getAvatar());
            if (user.getCoverImage() != null)
                targetUser.setCoverImage(user.getCoverImage());
            userRepository.save(targetUser);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 找到當前用戶id
    public Integer getSelfId() {
        User user = getSelf();
        return user.getUserId();
    }

    // 找到當前用戶 Role
    public String getSelfRole() {
        User user = getSelf();
        Role role = user.getRole();
        return role.name();
    }

    // 根據 id 找用戶暱稱
    public String getNicknameById(Integer id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getNickname();
    }

//    找到當前用戶
    public User getSelf() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return userRepository.findByEmail(authentication.getName()).orElseThrow();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
