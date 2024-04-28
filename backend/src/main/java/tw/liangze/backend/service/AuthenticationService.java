package tw.liangze.backend.service;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tw.liangze.backend.model.AuthenticationResponse;
import tw.liangze.backend.model.User;
import tw.liangze.backend.repository.UserLogRepository;
import tw.liangze.backend.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserLogRepository userLogRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final HttpServletRequest servletRequest;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserLogRepository userLogRepository, JwtService jwtService, AuthenticationManager authenticationManager, HttpServletRequest servletRequest) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userLogRepository = userLogRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.servletRequest = servletRequest;
    }


    public AuthenticationResponse register(User request) {
//        檢查輸入資料是否為空
        if (request.getEmail() == null || request.getPassword() == null || request.getNickname() == null ||
                request.getPassword().isEmpty() || request.getNickname().isEmpty()) {
            return null;
        }
//        檢查電子信箱是否已被註冊
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return null;
        }
//        註冊
        userRepository.insertUser(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getNickname());

        User NewUser = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(NewUser);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(Map<String, String> request) {
        String status = "登入成功";
        String username =request.get("username");
        String password = request.get("password");

        User user = null;
        if (isValidEmail(username)) { // 判断是否为有效的邮箱
            user = userRepository.findByEmailAndDeletedFalse(username).orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            user = userRepository.findByPhoneAndDeletedFalse(username).orElseThrow(() -> new RuntimeException("User not found"));
        }
        int userId = user.getUserId();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );

            String token = jwtService.generateToken(user);

            return new AuthenticationResponse(token);
        } catch (AuthenticationException e) {
            status = "登入失敗";
            throw new BadCredentialsException("登入失敗", e);
        } finally {
            String ip = servletRequest.getHeader("X-Forwarded-For");
            if (ip == null) {
                ip = "unknown";
            }
            userLogRepository.insertUserLog(userId, ip, status);
        }
    }

    public boolean checkPhone(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }

    public boolean checkEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean isValidEmail(String email) {
        return email.contains("@");
    }

}
