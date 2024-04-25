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
import tw.liangze.backend.repository.UserRepository;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final HttpServletRequest servletRequest;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, HttpServletRequest servletRequest) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.servletRequest = servletRequest;
    }


    public AuthenticationResponse register(User request) {
        if (request == null || request.getPassword() == null || request.getNickname() == null ||
                request.getPassword().isEmpty() || request.getNickname().isEmpty()) {
            return new AuthenticationResponse("register error");
        }

        if (checkPhone(request.getPhone())) {
            return new AuthenticationResponse("Phone number already exists");
        }

        userRepository.insertUser(request.getPhone(), passwordEncoder.encode(request.getPassword()), request.getNickname());

        Optional<User> optUser = userRepository.findByPhoneAndDeletedFalse(request.getPhone());
        User saveUser = optUser.orElseThrow();
        String token = jwtService.generateToken(saveUser);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(User request) {
        String status = "登入成功";
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getPhone(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByPhoneAndDeletedFalse(request.getPhone()).orElseThrow();
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
            userRepository.insertUserLog(request.getPhone(), ip, status);
        }
    }

    public boolean checkPhone(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }


}
