package xyz.raygetoffer.remoting.communication.netty.client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储channel的容器，以哈希表的形式存储连接的channel，key为服务器地址，value为对应的channel
 * 为了解决线程安全问题，所以使用ConcurrentHashMap
 *
 * @author mingruiwu
 * @create 2022/7/4 10:43
 * @description
 */
@Slf4j
@Component
public class ChannelContainer {
    private final Map<String, Channel> CHANNEL_MAP;

    public ChannelContainer() {
        CHANNEL_MAP = new ConcurrentHashMap<>();
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        Channel channel = CHANNEL_MAP.get(key);
        // 确定key对应的channel是否存在
        if (channel != null) {
            // 进一步确认connection是否可用，如果不可用，则从map中移除对应的channel
            if (channel.isActive()) {
                return channel;
            } else {
                removeChannel(inetSocketAddress);
            }
        }
        return null;

    }

    public void setChannel(InetSocketAddress inetSocketAddress, Channel channel) {
        String key = inetSocketAddress.toString();
        CHANNEL_MAP.put(key, channel);
    }

    public void removeChannel(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        CHANNEL_MAP.remove(key);
    }
}
