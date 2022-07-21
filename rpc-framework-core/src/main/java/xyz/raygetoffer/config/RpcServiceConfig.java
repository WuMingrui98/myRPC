package xyz.raygetoffer.config;

import lombok.*;

/**
 * Rpc服务的配置对象，用来区分具体的服务
 *
 * @author mingruiwu
 * @create 2022/7/9 17:05
 * @description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RpcServiceConfig {
    /**
     * 服务的版本
     */
    private String version = "";

    /**
     * 服务所在的组，当一个接口有多个实现，可以通过group来加以区分
     */
    private String group = "";

    /**
     * 目标服务
     */
    private Object service;

    public String getRpcServiceName() {
        return getServiceName() + group + version;
    }

    public String getServiceName() {
        return service.getClass().getInterfaces()[0].getCanonicalName();
    }

}
