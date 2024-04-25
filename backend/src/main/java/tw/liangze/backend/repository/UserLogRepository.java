package tw.liangze.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.liangze.backend.model.UserLog;

import java.util.List;


public interface UserLogRepository extends JpaRepository <UserLog, Integer> {

    List<UserLog> findTop50ByUserIdOrderByTimeDesc(int userId);

}
