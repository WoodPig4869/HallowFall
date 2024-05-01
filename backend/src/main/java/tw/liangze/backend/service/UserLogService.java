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
    private  final UserService userService;

    public UserLogService(UserLogRepository userLogRepository, UserService userService) {
        this.userLogRepository = userLogRepository;
        this.userService = userService;
    }


    public List<UserLog> findTop50() {
        //        找到當前用戶userId
        Integer targetUserId = userService.getSelfId();
        return userLogRepository.findTop50ByUserIdOrderByTimeDesc(targetUserId);
    }
}
