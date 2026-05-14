package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserBean;
import com.example.demo.service.UserService;
import com.example.demo.util.ResponseEntityBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "使用者管理 (User Management)", description = "提供使用者增、刪、改、查相關 API")
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "獲取所有使用者列表", description = "從資料庫中檢索所有註冊的使用者資訊")
    @GetMapping
    public ResponseEntity<com.example.demo.dto.ApiResponse<List<UserBean>>> getAllUsers() {
        try {
            logger.info("收到請求：查詢所有使用者");
            List<UserBean> result = userService.getAllUsers();
            return ResponseEntityBuilder.<List<UserBean>>success()
                    .message("查詢成功")
                    .data(result)
                    .build();
        } catch (Exception e) {
            logger.error("查詢所有使用者時發生系統錯誤: ", e);
            return ResponseEntityBuilder.<List<UserBean>>error()
                    .message("系統繁忙，請稍後再試")
                    .build();
        }
    }

    @Operation(summary = "根據 ID 查詢使用者", description = "輸入整數 ID 以獲得該使用者的詳細資料")
    @GetMapping("/{id}")
    public ResponseEntity<com.example.demo.dto.ApiResponse<UserBean>> getUser(
            @Parameter(description = "使用者唯一識別碼", example = "1") @PathVariable int id) {
        try {
            logger.info("收到請求：查詢 ID 為 {} 的使用者", id);
            return userService.getUserById(id)
                    .map(userBean -> ResponseEntityBuilder.<UserBean>success()
                            .message("查詢成功")
                            .data(userBean)
                            .build())
                    .orElseGet(() -> {
                        logger.warn("查詢結果：找不到 ID 為 {} 的使用者", id);
                        return ResponseEntityBuilder.<UserBean>error()
                                .message("找不到該使用者")
                                .build();
                    });
        } catch (Exception e) {
            logger.error("查詢 ID {} 時發生未知錯誤: ", id, e);
            return ResponseEntityBuilder.<UserBean>error()
                    .message("伺服器內部錯誤")
                    .build();
        }
    }

    @Operation(summary = "新增使用者", description = "傳入 JSON 格式的使用者資訊以建立帳號")
    @PostMapping
    public ResponseEntity<com.example.demo.dto.ApiResponse<String>> createUser(@Valid @RequestBody UserBean userBean) {
        try {
            logger.info("收到請求：準備建立新使用者: {}", userBean.getName());
            userService.createUser(userBean);
            return ResponseEntityBuilder.<String>success()
                    .message("使用者建立成功")
                    .build();
        } catch (Exception e) {
            logger.error("建立使用者時發生錯誤: ", e);
            return ResponseEntityBuilder.<String>error()
                    .message("新增失敗，請聯絡管理員")
                    .build();
        }
    }

    @Operation(summary = "更新使用者資料", description = "根據 ID 修改現有使用者的資訊")
    @PutMapping("/{id}")
    public ResponseEntity<com.example.demo.dto.ApiResponse<String>> updateUser(
            @Parameter(description = "要更新的使用者 ID", example = "1") @PathVariable int id,
            @Valid @RequestBody UserBean userBean) {
        try {
            logger.info("收到請求：嘗試更新使用者 ID: {}", id);
            if (userService.updateUser(id, userBean)) {
                return ResponseEntityBuilder.<String>success()
                        .message("更新成功")
                        .build();
            } else {
                logger.warn("更新失敗：找不到 ID 為 {} 的使用者", id);
                return ResponseEntityBuilder.<String>error()
                        .message("找不到該使用者，更新失敗")
                        .build();
            }
        } catch (Exception e) {
            logger.error("更新 ID {} 時發生錯誤: ", id, e);
            return ResponseEntityBuilder.<String>error()
                    .message("更新過程發生異常")
                    .build();
        }
    }

    @Operation(summary = "刪除使用者", description = "根據 ID 從系統中移除使用者紀錄")
    @DeleteMapping("/{id}")
    public ResponseEntity<com.example.demo.dto.ApiResponse<String>> deleteUser(
            @Parameter(description = "要刪除的使用者 ID", example = "1") @PathVariable int id) {
        try {
            logger.info("收到請求：執行刪除操作，目標 ID: {}", id);
            if (userService.deleteUser(id)) {
                return ResponseEntityBuilder.<String>success()
                        .message("刪除成功")
                        .build();
            } else {
                logger.warn("刪除失敗：ID 為 {} 的使用者不存在", id);
                return ResponseEntityBuilder.<String>error()
                        .message("刪除失敗，該使用者不存在")
                        .build();
            }
        } catch (Exception e) {
            logger.error("刪除 ID {} 時發生錯誤: ", id, e);
            return ResponseEntityBuilder.<String>error()
                    .message("刪除過程發生異常")
                    .build();
        }
    }
}
