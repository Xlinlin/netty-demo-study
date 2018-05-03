/*
 * Winner 
 * 文件名  :Client.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.demo.stick.client;

import com.purcotton.netty.demo.stick.client.handler.BaseClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
public class Client
{
    private String host = "127.0.0.1";
    private int port;

    public Client(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public void start()
    {
        // 客户端需要一个时间群组即可
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            Bootstrap client = new Bootstrap();
            client.group(group);
            // 指定 Channel 的类型. 因为是客户端, 因此使用了 NioSocketChannel.
            client.channel(NioSocketChannel.class);
            // option，提供了一系列的TCP参数
            client.option(ChannelOption.TCP_NODELAY, true);
            client.handler(new ChannelInitializer<SocketChannel>()
            {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception
                {
                    // ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    ch.pipeline().addLast(new StringDecoder());
                    // 客户端具体处理器
                    ch.pipeline().addLast(new BaseClientHandler());
                }
            });

            ChannelFuture future = client.connect(host, port).sync();
            future.channel().writeAndFlush("Hello Netty Server ,I am a common client");
            future.channel().closeFuture().sync();
        }
        catch (Exception e)
        {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args)
    {
        Client client = new Client("localhost", 8888);
        client.start();
    }
}
