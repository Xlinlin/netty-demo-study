/*
 * Winner 
 * 文件名  :Server.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.xiao.netty.demo.common.server;

import java.net.InetSocketAddress;

import com.xiao.netty.demo.common.basic.Basic;
import com.xiao.netty.demo.common.basic.NettyConnection;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * [简要描述]:Netty 服务端<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since JDK-1.8
 */
public class Server extends Basic implements NettyConnection
{
    private int port;

    /**
     * [简要描述]:服务端构造器<br/>
     * [详细描述]:<br/>
     *
     * @author llxiao
     * @param port
     *            端口
     * @param initializer
     *            ChannelInitializer
     */
    public Server(int port, ChannelInitializer<SocketChannel> initializer)
    {
        this.port = port;
        this.initializer = initializer;
    }

    /**
     * [简要描述]:建立服务端连接<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @see com.xiao.netty.demo.common.basic.NettyConnection#start()
     */
    @Override
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
            server.childHandler(initializer);
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

}
