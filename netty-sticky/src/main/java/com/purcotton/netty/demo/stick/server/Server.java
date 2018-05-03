/*
 * Winner 
 * 文件名  :Server.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.demo.stick.server;

import java.net.InetSocketAddress;

import com.purcotton.netty.demo.stick.server.handler.BaseServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
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
                    // 利用行标记解决粘包问题，指定包的大小必须比实际的包的大小要大。否则会出现TooLongFrameException
                    // ch.pipeline().addLast(new LineBasedFrameDecoder(2048));
                    // 定长解码器 解决单次发送的包内容过多的情况，粘包的现象：
                    ch.pipeline().addLast(new FixedLengthFrameDecoder(23));

                    // 编解码
                    ch.pipeline().addLast(new StringDecoder());

                    // 具体业务逻辑 设置数据的处理器
                    // ch.pipeline().addLast(new HelloWorldServerHandler());
                    ch.pipeline().addLast(new BaseServerHandler());
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
