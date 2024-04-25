package tw.liangze.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import tw.liangze.backend.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Integer> {

    Optional<User> findByPhone(String phone);

    Optional<User> findByPhoneAndDeletedFalse(String phone);

    Optional<User> findByUserIdAndDeletedFalse(Integer userId);

    @Procedure(name = "insertUser")
    void insertUser(String newPhone, String newPassword, String newNickname);

    @Procedure(name="insertUserLog")
    void insertUserLog(String newPhone, String newIP,String newStatus);

    Iterable<User> findAllByDeletedFalse();
}
