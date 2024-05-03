package tw.liangze.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "驗證控制", description = "管理登入及註冊等驗證相關的功能")
@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @Operation(summary = "註冊會員", description = "根據User物件註冊會員(電子信箱/暱稱/密碼)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "註冊成功，返回驗證訊息"),
            @ApiResponse(responseCode = "400", description = "註冊失敗")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request) {
        AuthenticationResponse response = authService.register(request);
        if (response == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("註冊失敗"); // 註冊失敗，返回 400 狀態碼和錯誤訊息
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 註冊成功，返回 201 狀態碼
    }

    @Operation(summary = "登入會員", description = "根據電子信箱或電話，以及密碼進行登入")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "登入成功，返回驗證訊息"),
            @ApiResponse(responseCode = "400", description = "登入失敗")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
            AuthenticationResponse response = authService.authenticate(request);
            if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("登入失敗"); // 登入失敗，返回 400 狀態碼和錯誤訊息
        }
        return ResponseEntity.ok(response); // 登入成功，返回 200 狀態碼
    }
}
