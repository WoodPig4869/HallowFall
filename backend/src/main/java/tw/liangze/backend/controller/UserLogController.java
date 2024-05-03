package tw.liangze.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "紀錄控制", description = "管理UserLog物件")
@RestController
@RequestMapping("/userlog")
public class UserLogController {
    private final UserLogService userLogService;

    public UserLogController(UserLogService userLogService) {
        this.userLogService = userLogService;
    }

    @Operation(summary = "查詢帳號紀錄", description = "查詢當前用戶的帳號紀錄")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "回傳帳號紀錄"),
            @ApiResponse(responseCode = "404", description = "查無資料")
    })
    @GetMapping("")
    public ResponseEntity<List<UserLog>> getUserLogs(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        try {
            List<UserLog> userLogs = userLogService.findTop50();
            return ResponseEntity.ok(userLogs);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }
}
