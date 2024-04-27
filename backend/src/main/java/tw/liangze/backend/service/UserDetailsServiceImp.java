package tw.liangze.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tw.liangze.backend.repository.UserRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    //構造方法注入UserRepository
    private final UserRepository repository;

    public UserDetailsServiceImp(UserRepository repository) {
        this.repository = repository;
    }

    //實現loadUserByUsername方法，用來取得使用者的資料
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean isValidEmail = isValidEmail(username);
        if (isValidEmail) {
            return repository.findByEmailAndDeletedFalse(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        } else {
            return repository.findByPhoneAndDeletedFalse(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        }
    }
    // 判斷是否符合 email 格式的方法，可以根據實際需求進行調整
    private boolean isValidEmail(String email) {
        // 簡單檢查是否包含 @ 符號
        return email.contains("@");
    }
}
