package tw.liangze.backend.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tw.liangze.backend.model.User;
import tw.liangze.backend.model.UserLog;
import tw.liangze.backend.repository.UserLogRepository;
import tw.liangze.backend.repository.UserRepository;

import java.util.List;

@Service
public class UserLogService {

    private  final UserLogRepository userLogRepository;
    private  final UserRepository userRepository;

    public UserLogService(UserLogRepository userLogRepository, UserRepository userRepository) {
        this.userLogRepository = userLogRepository;
        this.userRepository = userRepository;
    }

    public List<UserLog> findTop50() {
        //        找到當前用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User targetUser = userRepository.findByPhone(authentication.getName()).orElseThrow();
        Integer targetUserId = targetUser.getUserId();

        return userLogRepository.findTop50ByUserIdOrderByTimeDesc(targetUserId);
    }
}
