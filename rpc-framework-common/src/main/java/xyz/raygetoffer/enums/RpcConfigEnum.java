package xyz.raygetoffer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mingruiwu
 * @create 2022/7/6 10:58
 * @description
 */
@AllArgsConstructor
@Getter
public enum RpcConfigEnum {
    RPC_CONFIG_PATH("rpc.properties"),
    ZK_ADDRESS("rpc.zookeeper.address");

    private final String propertyValue;
}
