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

    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paraClasses;
    // version(服务版本)主要为后续不兼容升级提供可能
    private String version;
    // group主要用于处理一个接口有多个实现类
    private String group;


    public String getRpcServiceName() {
        return getInterfaceName() + getGroup() + getVersion();
    }
}
