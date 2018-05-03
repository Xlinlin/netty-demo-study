/*
 * Winner 
 * 文件名  :Server.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.heartbeat.simple.server;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import com.purcotton.netty.heartbeat.simple.server.handler.HeartBeatServerHandler;

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
import io.netty.handler.timeout.IdleStateHandler;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 * IdleStateHandler这个类会根据你设置的超时参数的类型和值，循环去检测channelRead和write方法多久没有被调用了，<br>
 * 如果这个时间超过了你设置的值，那么就会触发对应的事件，read触发read，write触发write，all触发all <br>
 * 如果超时了，则会调用userEventTriggered方法，且会告诉你超时的类型 <br>
 * 如果没有超时，则会循环定时检测，除非你将IdleStateHandler移除Pipeline <br>
 * 可以对三种类型的心跳检测：读、写、所有类型<br>
 * 构造参数前三个的参数解释：<br>
 * 1）readerIdleTime：为读超时时间（即测试端一定时间内未接受到被测试端消息）<br>
 * 2）writerIdleTime：为写超时时间（即测试端一定时间内向被测试端发送消息）<br>
 * 3）allIdleTime：所有类型的超时时间<br>
 * 
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
public class Server
{
    private int port;

    public Server(int port)
    {
        this.port = port;
    }

    public void start()
    {
        // NIO 的EventLoopGroup 用于处理客户端的连接请求
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // NIO 的EventLoopGroup 用于处理与各个客户端连接的 IO 操作
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            // ServerBootstrap创建服务
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup);
            server.localAddress(new InetSocketAddress(port));
            // 指定 Channel 的类型. 因为是服务器端, 因此使用了 NioServerSocketChannel
            server.channel(NioServerSocketChannel.class);
            // handler 是在 accept 阶段起作用, 它处理客户端的连接请求.
            // server.handler(ChannelHandler);
            // childHandler 是在客户端连接建立以后起作用, 它负责客户端连接的 IO 交互.
            server.childHandler(new ChannelInitializer<SocketChannel>()
            {
                // 客户端连接 Channel 注册后, 就会触发 ChannelInitializer.initChannel 方法的调用
                @Override
                protected void initChannel(SocketChannel ch) throws Exception
                {
                    // 可以每隔5秒检测一下服务端的读超时，即没5S检测客户端有没有发送消息过来
                    ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));

                    ch.pipeline().addLast("decoder", new StringDecoder());
                    ch.pipeline().addLast("encoder", new StringEncoder());

                    // 具体业务逻辑 设置数据的处理器
                    ch.pipeline().addLast(new HeartBeatServerHandler());
                }
            });
            // 提供了一系列的TCP参数
            server.option(ChannelOption.SO_BACKLOG, 128);
            // 长连接
            server.childOption(ChannelOption.SO_KEEPALIVE, true);

            // 异步开始接收服务
            ChannelFuture future = server.bind(port).sync();

            System.out.println("Server start listen at " + port);
            future.channel().closeFuture().sync();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args)
    {
        Server server = new Server(8888);
        server.start();
    }
}
