package tw.liangze.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tw.liangze.backend.model.Role;
import tw.liangze.backend.model.User;
import tw.liangze.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest servletRequest;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, HttpServletRequest servletRequest) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.servletRequest = servletRequest;
    }

    public boolean updateUser(User user) {
        try {
            //        找到當前用戶
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User targetUser = userRepository.findByPhone(authentication.getName()).orElseThrow();
            //        更新用戶資料
            if (user.getPassword() != null) {
                String ip = servletRequest.getHeader("X-Forwarded-For");
                if (ip == null) {
                    ip = "unknown";
                }
                userRepository.insertUserLog(targetUser.getPhone(), ip, "修改密碼");
                targetUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getEmail() != null)
                targetUser.setEmail(user.getEmail());
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByPhone(authentication.getName()).orElseThrow();
        return user.getUserId();
    }

    // 找到當前用戶 Role
    public String getSelfRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByPhone(authentication.getName()).orElseThrow();
        Role role = user.getRole();
        return role.name();
    }

    // id 轉換成用戶暱稱
    public String getNicknameById(Integer id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getNickname();
    }
}
