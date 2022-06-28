package xyz.raygetoffer.remoting.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author mingruiwu
 * @create 2022/6/28 13:54
 * @description 用于传递rpc响应的dto
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = -6250729106512286225L;
    int message;
}
