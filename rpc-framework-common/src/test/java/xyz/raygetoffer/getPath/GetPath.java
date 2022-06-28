package xyz.raygetoffer.getPath;

/**
 * @author mingruiwu
 * @create 2022/6/27 22:04
 * @description
 */
public class GetPath {
    public static void main(String[] args) {
        // /Users/mingruiwu/Desktop/RPC/myRPC/rpc-framework-common/target/test-classes/xyz/raygetoffer/getPath/
        String path1 = new GetPath().getClass().getResource("").getPath();
        System.out.println(path1);
        // / Users/mingruiwu/Desktop/RPC/myRPC/rpc-framework-common/target/test-classes/
        String path2 = new GetPath().getClass().getResource("/").getPath();
        System.out.println(path2);
        // /Users/mingruiwu/Desktop/RPC/myRPC/rpc-framework-common/target/test-classes/
        String path3 = new GetPath().getClass().getClassLoader().getResource("").getPath();
        System.out.println(path3);
    }
}
