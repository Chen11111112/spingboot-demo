package com.example.demo.dao;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User 資料存取層 (DAO)
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // 根據使用者名稱查詢（Spring Data JPA 會自動生成實作邏輯）
    Optional<User> findByName(String name);

    // 查詢所有啟用的使用者
    Iterable<User> findAllByEnableTrue();
}
