package tw.liangze.backend.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tw.liangze.backend.model.UserLog;
import tw.liangze.backend.service.UserLogService;

import java.util.List;


@RestController
@RequestMapping("/userlog")
public class UserLogController {
    private final UserLogService userLogService;

    public UserLogController(UserLogService userLogService) {
        this.userLogService = userLogService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserLog>> getUserLogs(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        List<UserLog> userLogs = userLogService.findTop50();
        return ResponseEntity.ok(userLogs);
    }
}
