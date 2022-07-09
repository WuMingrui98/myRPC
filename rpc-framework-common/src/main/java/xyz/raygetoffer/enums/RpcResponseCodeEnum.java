package xyz.raygetoffer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author mingruiwu
 * @create 2022/7/4 09:43
 * @description
 */
@Getter
@ToString
@AllArgsConstructor
public enum RpcResponseCodeEnum {
    SUCCESS(200, "The remote call is successful"),
    FAIL(500, "The remote call is failed");

    private final int code;
    private final String message;
}
