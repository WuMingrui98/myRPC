package xyz.raygetoffer;

import lombok.*;

import java.io.Serializable;

/**
 * @author mingruiwu
 * @create 2022/8/7 17:00
 * @description
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class Hello implements Serializable {
    private static final long serialVersionUID = -8573717388821013095L;
    private String message;
}
