package xyz.raygetoffer.remoting.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author mingruiwu
 * @create 2022/6/28 13:52
 * @description 用于传递rpc请求的dto
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 5684991405913109601L;
    private String interfaceName;
    private String methodName;
}
