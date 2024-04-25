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
        //使用者查詢，username為spring security 登入帳號預設變數名，Phone 為此專案應用的登入帳號
        return repository.findByPhoneAndDeletedFalse(username)
                //找不到使用者，則拋出 UsernameNotFoundException 異常
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
