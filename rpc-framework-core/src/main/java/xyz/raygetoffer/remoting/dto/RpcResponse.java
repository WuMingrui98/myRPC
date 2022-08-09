package xyz.raygetoffer.remoting.dto;

import lombok.*;
import xyz.raygetoffer.enums.RpcResponseCodeEnum;

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
    private String requestId;
    private Integer code;
    private String message;
    private T data;

    /**
     * 响应成功，并生成一个对应成功的RpcResponse
     * @param data          应返回的data
     * @param requestId     请求id
     * @param <T>           返回data的类型
     * @return              封装好的RpcResponse对象
     */
    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> rpcResponse = new RpcResponse<>();
        rpcResponse.setCode(RpcResponseCodeEnum.SUCCESS.getCode());
        rpcResponse.setMessage(RpcResponseCodeEnum.SUCCESS.getMessage());
        rpcResponse.setData(data);
        rpcResponse.setRequestId(requestId);
        return rpcResponse;
    }


    public static <T> RpcResponse<T> fail(String requestId) {
        RpcResponse<T> rpcResponse = new RpcResponse<>();
        rpcResponse.setCode(RpcResponseCodeEnum.FAIL.getCode());
        rpcResponse.setMessage(RpcResponseCodeEnum.FAIL.getMessage());
        return rpcResponse;
    }
}
