package xyz.raygetoffer.serialize;

import xyz.raygetoffer.remoting.dto.RpcResponse;

import java.net.InetSocketAddress;

/**
 * @author mingruiwu
 * @create 2022/6/28 11:19
 * @description
 */
public class TestMain {
    public static void main(String[] args) {
        RpcResponse<Object> response = RpcResponse.builder().message("123").build();
//        ISerializer serializer=new JdkSerializer();
//        ISerializer serializer = new KryoSerializer();
//        ISerializer serializer = new ProtostuffSerializer();
//        ISerializer serializer = new HessianSerializer();
//        byte[] serialize = serializer.serialize(response);
//        System.out.println("-----------:"+new String(serialize));
//        System.out.println(serializer.deserialize(RpcResponse.class, serialize));

        System.out.println(new InetSocketAddress("127.0.0.1", 88));
    }
}
