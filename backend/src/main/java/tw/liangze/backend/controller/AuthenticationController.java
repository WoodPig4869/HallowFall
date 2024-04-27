package tw.liangze.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tw.liangze.backend.model.AuthenticationResponse;
import tw.liangze.backend.model.User;
import tw.liangze.backend.service.AuthenticationService;

import java.util.List;
import java.util.Map;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * 註冊
     *
     * @param request 包含使用者名稱、密碼、電子郵件
     * @return 註冊成功返回 http status 201，失敗返回 400，並攜帶 AuthenticationResponse 物件，包含 JWT
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request) {
        AuthenticationResponse response = authService.register(request);
        if (response == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("註冊失敗"); // 註冊失敗，返回 400 狀態碼和錯誤訊息
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 註冊成功，返回 201 狀態碼
    }

    /**
     * 登入
     *
     * @param request 包含密碼、電子郵件
     * @return 註冊成功返回 http status 200，失敗返回 400，並攜帶 AuthenticationResponse 物件，包含 JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        AuthenticationResponse response = authService.authenticate(request);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("登入失敗"); // 登入失敗，返回 400 狀態碼和錯誤訊息
        }
        return ResponseEntity.ok(response); // 登入成功，返回 200 狀態碼
    }
}
