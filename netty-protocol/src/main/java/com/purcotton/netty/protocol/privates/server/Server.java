/*
 * Winner 
 * 文件名  :Server.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.protocol.privates.server;

import java.net.InetSocketAddress;

import com.purcotton.netty.protocol.privates.common.CustomDecoder;
import com.purcotton.netty.protocol.privates.common.CustomEncoder;
import com.purcotton.netty.protocol.privates.server.handler.BaseServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

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

    private static final int MAX_FRAME_LENGTH = 1024 * 1024;
    // 指的就是我们这边CustomMsg中length这个属性的大小，我们这边是int型，所以是4
    private static final int LENGTH_FIELD_LENGTH = 4;
    // 指的就是我们这边length字段的起始位置，因为前面有type和flag两个属性，且这两个属性都是byte，两个就是2字节，所以偏移量是2
    private static final int LENGTH_FIELD_OFFSET = 2;
    // 指的是length这个属性的值，假如我们的body长度是40，有时候，有些人喜欢将length写成44，因为length本身还占有4个字节，这样就需要调整一下，
    // 那么就需要-4，我们这边没有这样做，所以写0就可以了那么就需要-4，我们这边没有这样做，所以写0就可以了
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 0;

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
                    // 自定义协议解码器
                    ch.pipeline().addLast(new CustomDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH,
                            LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP, false));
                    // 自定义协议编码器
                    ch.pipeline().addLast(new CustomEncoder());
                    // 具体业务逻辑 设置数据的处理器
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
