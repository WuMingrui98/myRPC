package xyz.raygetoffer.loadbalance;

import xyz.raygetoffer.remoting.dto.RpcRequest;

import java.util.List;

/**
 * @author mingruiwu
 * @create 2022/7/6 14:50
 * @description
 */
public abstract class AbstractLoadBalance implements ILoadBalance {
    @Override
    public String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest) {
        if (serviceUrlList.isEmpty()) {
            return null;
        }
        if (serviceUrlList.size() == 1) {
            return serviceUrlList.get(0);
        }
        return doSelect(serviceUrlList, rpcRequest);
    }

    protected abstract String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest);
}
