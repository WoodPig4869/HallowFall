package tw.liangze.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import tw.liangze.backend.model.UserLog;

import java.util.List;


public interface UserLogRepository extends JpaRepository <UserLog, Integer> {

    List<UserLog> findTop50ByUserIdOrderByTimeDesc(int userId);

    @Procedure(name="insertUserLog")
    void insertUserLog(int newUserId, String newIP,String newStatus);

}
