package com.example.demo.service.impl;

import com.example.demo.dao.UserRepository;
import com.example.demo.dto.UserBean;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    // 注入真正的資料存取層 (DAO)
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserBean> getAllUsers() {
        logger.info("執行 getAllUsers: 從資料庫讀取資料");
        // 從資料庫抓取所有 Entity，並轉換成 Bean 回傳
        return userRepository.findAll().stream().map(user -> {
            UserBean bean = new UserBean();
            BeanUtils.copyProperties(user, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<UserBean> getUserById(int id) {
        logger.debug("正在資料庫尋找使用者，ID: {}", id);
        return userRepository.findById(id).map(user -> {
            UserBean bean = new UserBean();
            BeanUtils.copyProperties(user, bean);
            return bean;
        });
    }

    @Override
    public void createUser(UserBean userBean) {
        logger.info("準備建立新使用者: {}", userBean.getName());

        User user = new User();
        // 將前端傳來的 Bean 轉為資料庫用的 Entity
        BeanUtils.copyProperties(userBean, user);

        // 儲存時，UserListener 會自動補上 createTime 與 enable 預設值
        userRepository.save(user);
        logger.info("使用者存入資料庫成功");
    }

    @Override
    public boolean updateUser(int id, UserBean userBean) {
        logger.info("嘗試更新使用者 ID: {}", id);
        return userRepository.findById(id).map(existingUser -> {
            // 只更新名稱 (或其他允許修改的欄位)
            existingUser.setName(userBean.getName());

            // 儲存時，UserListener 會自動觸發 @PreUpdate 更新 modifyTime
            userRepository.save(existingUser);
            return true;
        }).orElseGet(() -> {
            logger.warn("更新失敗：找不到 ID 為 {} 的使用者", id);
            return false;
        });
    }

    @Override
    public boolean deleteUser(int id) {
        logger.info("執行刪除操作，目標 ID: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}