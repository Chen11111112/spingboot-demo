package com.example.demo.controller;

import com.example.demo.dto.UserBean;
import com.example.demo.service.UserService;
import com.example.demo.util.ResponseEntityBuilder;
import com.example.demo.dto.ApiResponse; // 確保有對應的泛型包裝類別
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserBean>>> getAllUsers() {
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserBean>> getUser(@PathVariable int id) {
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

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createUser(@Valid @RequestBody UserBean userBean) {
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

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateUser(@PathVariable int id, @Valid @RequestBody UserBean userBean) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable int id) {
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