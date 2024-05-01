package tw.liangze.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.liangze.backend.model.User;
import tw.liangze.backend.repository.UserRepository;
import tw.liangze.backend.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * 查詢 Email 是否存在
     * @param email
     * @return 存在回傳 http status 200, 不存在回傳 http status 404
     */
    @GetMapping("/checkEmail/{email}")
    public ResponseEntity<String> checkEmail(@PathVariable("email") String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * 查詢 Phone 是否存在
     * @param phone
     * @return 存在回傳 http status 200, 不存在回傳 http status 404
     */
    @GetMapping("/checkPhone/{phone}")
    public ResponseEntity<String> checkPhone(@PathVariable("phone") String phone) {
        Optional<User> user = userRepository.findByPhone(phone);
        if (user.isPresent()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //    查詢全部會員
//    @GetMapping("/all")
//    public Iterable<User> getAllUsers() {
//        return userRepository.findAllByDeletedFalse();
//    }

    //    根據 userId查詢會員，id為0表示當前用戶
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int userId) {
        try{
            return ResponseEntity.ok(userService.findById(userId));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }


    //    修改自己
    @PutMapping("/")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        return userService.updateUser(user)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
