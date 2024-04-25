package tw.liangze.backend.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tw.liangze.backend.model.User;
import tw.liangze.backend.repository.UserRepository;
import tw.liangze.backend.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }
//查詢帳號(包含已刪除)，存在回傳True，不存在回傳False
    @GetMapping("/check/phone/{phone}")
    public boolean getUser(@PathVariable("phone") String phone) {
        Optional<User> userOptional = userRepository.findByPhone(phone);
        return userOptional.isPresent(); // 如果有值則返回 true，否則返回 false
    }
//    檢查是否已登入
    @GetMapping("/check/login")
    public boolean checkLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.isAuthenticated();
        }
        return false;
    }
//    查詢全部會員
    @GetMapping("/api")
    public Iterable<User> getAllUsers(){
        return userRepository.findAllByDeletedFalse();
    }
//    根據id查詢會員
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int userId){
        return userRepository.findByUserIdAndDeletedFalse(userId).orElseThrow();
    }
//    查詢自己
@GetMapping("/self")
public User getSelfUser(){
//    找到當前用戶
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userRepository.findByPhone(authentication.getName()).orElseThrow();
}

//    修改自己
@PutMapping("/self")
public boolean updateUser(@RequestBody User user){
        return userService.updateUser(user);
}
}
