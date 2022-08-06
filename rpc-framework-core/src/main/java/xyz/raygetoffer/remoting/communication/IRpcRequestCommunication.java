package xyz.raygetoffer.remoting.communication;

import xyz.raygetoffer.extension.SPI;
import xyz.raygetoffer.remoting.dto.RpcRequest;

/**
 * 发送rpc请求
 *
 * @author mingruiwu
 * @create 2022/6/29 16:51
 * @description
 */
@SPI(value = "netty")
public interface IRpcRequestCommunication {

    Object sendRpcRequest(RpcRequest rpcRequest);
}
