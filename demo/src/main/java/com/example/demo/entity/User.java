package com.example.demo.entity;

import com.example.demo.config.Config;
import com.example.demo.entity.listener.UserListener;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * User 實體類別
 */
@Entity
@Data // 自動生成 Getter/Setter/ToString
@Table(name = "users", schema = Config.DATABASE_NAME) // 引用 Config 定義的 Schema 名稱
@EntityListeners(UserListener.class) // 綁定先前建立的生命週期監聽器
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "enable")
    private Boolean enable;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "modify_time")
    private LocalDateTime modifyTime;
}