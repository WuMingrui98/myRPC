package xyz.raygetoffer.remoting.communication.impl.socket;

import lombok.extern.slf4j.Slf4j;
import xyz.raygetoffer.exception.RpcException;
import xyz.raygetoffer.remoting.communication.IRpcRequestCommunication;
import xyz.raygetoffer.remoting.dto.RpcRequest;
import xyz.raygetoffer.serialize.ISerializer;
import xyz.raygetoffer.serialize.impl.hessian.HessianSerializer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 客户端基于Socket传递RpcRequest
 *
 * @author mingruiwu
 * @create 2022/6/29 15:07
 * @description
 */
@Slf4j
public class SocketRpcClient implements IRpcRequestCommunication {
    String hostname = "127.0.0.1";
    int port = 6666;

    // 假设服务的ip和端口已知
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Send data to the server through the output stream
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // Read RpcResponse from the input stream
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("调用服务失败：", e);
        }
    }
}
