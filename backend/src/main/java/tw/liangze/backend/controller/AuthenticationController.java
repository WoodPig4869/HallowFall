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

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        try {
            AuthenticationResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response); // 登入成功，返回 200 狀態碼
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("登入失敗"); // 登入失敗，返回 400 狀態碼和錯誤訊息
        }
    }
}
