package xyz.raygetoffer.remoting.communication.impl.socket;

import lombok.extern.slf4j.Slf4j;
import xyz.raygetoffer.remoting.dto.RpcRequest;
import xyz.raygetoffer.remoting.dto.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author mingruiwu
 * @create 2022/6/29 17:35
 * @description
 */
@Slf4j
public class SocketRpcRequestHandlerRunnable implements Runnable {
    private final Socket socket;

    public SocketRpcRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        log.info("server handle message from client by thread: [{}]", Thread.currentThread().getName());
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            RpcResponse<String> rpcResponse = new RpcResponse<>("112");
            objectOutputStream.writeObject(rpcResponse);
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
