package xyz.raygetoffer.remoting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mingruiwu
 * @create 2022/7/4 16:46
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcMessage implements Serializable {
    private static final long serialVersionUID = 6732112668902545294L;

    private byte messageType;

    private byte codec;

    private byte compress;

    private int requestId;

    private Object data;
}
