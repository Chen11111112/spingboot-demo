package com.example.demo.service;

import com.example.demo.dto.UserBean;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // 取得所有使用者，回傳 Bean 列表
    List<UserBean> getAllUsers();

    // 根據 ID 取得特定使用者
    Optional<UserBean> getUserById(int id);

    // 建立使用者，接收前端傳來的 Bean
    void createUser(UserBean userBean);

    // 更新使用者，接收 ID 與包含新資料的 Bean
    boolean updateUser(int id, UserBean userBean);

    // 刪除使用者
    boolean deleteUser(int id);
}