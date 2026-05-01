package com.example.demo.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * User 資料傳輸物件 (DTO/Bean)
 * 用於 API 請求參數接收與校驗
 */
@Data
public class UserBean {

    // 定義常用的格式正規表示式
    private final String dateFormat = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
    private final String dateTimeFormat = "^(?:19|20)\\d{2}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[1-2][0-9]|3[0-1]) (?:[01][0-9]|2[0-3]):(?:[0-5][0-9]):(?:[0-5][0-9])$";


    @Null(message = "使用者 ID - 系統自動生成，不得事先填寫")
    private Integer id;

    @NotBlank(message = "姓名 - 為必填欄位")
    private String name;

    @Pattern(regexp = "^(true|false)$", message = "啟用狀態 - 格式錯誤 (必須為 true 或 false)")
    private String enable;

    @Pattern(regexp = dateTimeFormat, message = "建立時間 - 格式必須為 yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @Pattern(regexp = dateTimeFormat, message = "修改時間 - 格式必須為 yyyy-MM-dd HH:mm:ss")
    private String modifyTime;
}