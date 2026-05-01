package com.example.demo.entity.listener;

import com.example.demo.entity.User;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

public class UserListener {

    @PrePersist // 資料新增前的自動處理
    public void preSave(User user) {
        // 若為空則預設啟用
        if (user.getEnable() == null) {
            user.setEnable(true);
        }
        // 自動設定建立時間
        if (user.getCreateTime() == null) {
            user.setCreateTime(LocalDateTime.now());
        }
    }

    @PreUpdate // 資料更新前的自動處理
    public void preUpdate(User user) {
        // 更新時同步紀錄時間
        LocalDateTime time = LocalDateTime.now();
        user.setModifyTime(time);
    }
}