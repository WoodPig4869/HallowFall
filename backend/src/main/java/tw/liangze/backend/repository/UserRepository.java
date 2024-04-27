package tw.liangze.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import tw.liangze.backend.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Integer> {

    Optional<User> findByPhone(String phone);

    Optional<User> findByUserId(Integer userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneAndDeletedFalse(String phone);

    Optional<User> findByEmailAndDeletedFalse(String username);

    Optional<User> findByUserIdAndDeletedFalse(Integer userId);

    @Procedure(name = "insertUser")
    void insertUser(String newEmail, String newPassword, String newNickname);


    Iterable<User> findAllByDeletedFalse();


}
