package tw.liangze.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.liangze.backend.model.User;
import tw.liangze.backend.repository.UserRepository;
import tw.liangze.backend.service.UserService;

import java.util.Optional;
@Tag(name = "會員控制", description = "管理User物件")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Operation(summary = "查詢會員", description = "根據userId查詢會員，id為0表示當前用戶")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查詢成功，返回User物件"),
            @ApiResponse(responseCode = "404", description = "查詢失敗")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable int userId) {
        try{
            return ResponseEntity.ok(userService.findById(userId));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "查詢電子信箱", description = "查詢Email是否已被使用")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "此Email已被使用"),
            @ApiResponse(responseCode = "404", description = "此Email未被使用")
    })
    @GetMapping("/checkEmail/{email}")
    public ResponseEntity<String> checkEmail(@Parameter(description = "電子信箱") @PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "查詢手機號碼", description = "查詢Phone是否已被使用")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "此Phone已被使用"),
            @ApiResponse(responseCode = "404", description = "此Phone未被使用")
    })
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

    @Operation(summary = "修改會員", description = "修改會員(目標為當前當入的會員)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "修改成功"),
            @ApiResponse(responseCode = "400", description = "修改失敗")
    })
    @PutMapping("")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        return userService.updateUser(user)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
