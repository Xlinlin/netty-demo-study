/*
 * Winner 
 * 文件名  :HeartBeatServer.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.heartbeat.prod.server;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import com.purcotton.netty.heartbeat.prod.server.handler.AcceptorIdleStateTrigger;
import com.purcotton.netty.heartbeat.prod.server.handler.HeartBeatServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
public class HeartBeatServer
{
    private final AcceptorIdleStateTrigger idleStateTrigger = new AcceptorIdleStateTrigger();

    private int port;

    public HeartBeatServer(int port)
    {
        this.port = port;
    }

    public void start()
    {
        // netty也不是完全使用java 的logger，我们可以设置netty的loggerFactory来使用不同的日志框架。
        // InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup);
            server.channel(NioServerSocketChannel.class);
            // netty自带LoggingHandler,java的日志框架java logger
            server.handler(new LoggingHandler(LogLevel.INFO));
            server.localAddress(new InetSocketAddress(port));
            server.childHandler(new ChannelInitializer<SocketChannel>()
            {
                protected void initChannel(SocketChannel ch) throws Exception
                {
                    ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                    ch.pipeline().addLast(idleStateTrigger);
                    ch.pipeline().addLast("decoder", new StringDecoder());
                    ch.pipeline().addLast("encoder", new StringEncoder());
                    ch.pipeline().addLast(new HeartBeatServerHandler());
                };

            });
            server.option(ChannelOption.SO_BACKLOG, 128);
            server.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 绑定端口，开始接收进来的连接
            ChannelFuture future = server.bind(port).sync();

            System.out.println("Server start listen at " + port);
            future.channel().closeFuture().sync();
        }
        catch (Exception e)
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception
    {
        int port;
        if (args.length > 0)
        {
            port = Integer.parseInt(args[0]);
        }
        else
        {
            port = 8888;
        }
        new HeartBeatServer(port).start();
    }
}
