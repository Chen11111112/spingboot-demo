package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * User 資料傳輸物件 (DTO/Bean)
 */
@Data
@Schema(description = "使用者資料傳輸物件")
public class UserBean {

    // 格式定義保持不變
    private final String dateFormat = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
    private final String dateTimeFormat = "^(?:19|20)\\d{2}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[1-2][0-9]|3[0-1]) (?:[01][0-9]|2[0-3]):(?:[0-5][0-9]):(?:[0-5][0-9])$";

    @Schema(description = "使用者 ID (系統自動生成，新增時請留空)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Null(message = "使用者 ID - 系統自動生成，不得事先填寫")
    private Integer id;

    @Schema(description = "使用者姓名", example = "王小明", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "姓名 - 為必填欄位")
    private String name;

    @Schema(description = "帳號啟用狀態", example = "true", allowableValues = {"true", "false"})
    @Pattern(regexp = "^(true|false)$", message = "啟用狀態 - 格式錯誤 (必須為 true 或 false)")
    private String enable;

    @Schema(description = "建立時間", example = "2024-05-20 13:14:00", pattern = "yyyy-MM-dd HH:mm:ss")
    @Pattern(regexp = dateTimeFormat, message = "建立時間 - 格式必須為 yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @Schema(description = "最後修改時間", example = "2024-05-21 09:00:00", pattern = "yyyy-MM-dd HH:mm:ss")
    @Pattern(regexp = dateTimeFormat, message = "修改時間 - 格式必須為 yyyy-MM-dd HH:mm:ss")
    private String modifyTime;
}
