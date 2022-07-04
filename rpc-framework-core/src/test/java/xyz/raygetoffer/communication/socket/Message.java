package xyz.raygetoffer.communication.socket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mingruiwu
 * @create 2022/6/28 16:25
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    private static final long serialVersionUID = -5754244284344811811L;
    private String content;
}
