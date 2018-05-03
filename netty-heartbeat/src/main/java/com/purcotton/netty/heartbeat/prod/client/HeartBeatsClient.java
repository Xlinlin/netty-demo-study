/*
 * Winner 
 * 文件名  :HeartBeatsClient.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.heartbeat.prod.client;

import java.util.concurrent.TimeUnit;

import com.purcotton.netty.heartbeat.prod.client.handler.ConnectionWatchdog;
import com.purcotton.netty.heartbeat.prod.client.handler.ConnectorIdleStateTrigger;
import com.purcotton.netty.heartbeat.prod.client.handler.HeartBeatClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
public class HeartBeatsClient
{
    protected final HashedWheelTimer timer = new HashedWheelTimer();

    private Bootstrap boot;

    private final ConnectorIdleStateTrigger idleStateTrigger = new ConnectorIdleStateTrigger();

    public void connect(int port, String host) throws Exception
    {
        EventLoopGroup group = new NioEventLoopGroup();

        boot = new Bootstrap();
        boot.group(group).channel(NioSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO));

        // 重连处理
        final ConnectionWatchdog watchdog = new ConnectionWatchdog(boot, timer, port, host, true)
        {
            public ChannelHandler[] handlers()
            {
                return new ChannelHandler[]
                {
                        // 重连处理
                        this,
                        // 写检查
                        new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS),
                        // 心跳检测状态处理类
                        idleStateTrigger,
                        // 解码
                        new StringDecoder(),
                        // 编码
                        new StringEncoder(),
                        // 业务处理
                        new HeartBeatClientHandler()
                };
            }
        };

        ChannelFuture future;
        // 进行连接
        try
        {
            synchronized (boot)
            {
                boot.handler(new ChannelInitializer<Channel>()
                {
                    // 初始化channel
                    @Override
                    protected void initChannel(Channel ch) throws Exception
                    {
                        ch.pipeline().addLast(watchdog.handlers());
                    }
                });
                future = boot.connect(host, port);
            }

            // 以下代码在synchronized同步块外面是安全的
            future.sync();
        }
        catch (Throwable t)
        {
            throw new Exception("connects to  fails", t);
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        int port = 8888;
        if (args != null && args.length > 0)
        {
            try
            {
                port = Integer.valueOf(args[0]);
            }
            catch (NumberFormatException e)
            {
                // 采用默认值
            }
        }
        new HeartBeatsClient().connect(port, "127.0.0.1");
    }
}
