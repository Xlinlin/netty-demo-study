/*
 * Winner 
 * 文件名  :Client.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.heartbeat.simple.client;

import java.util.concurrent.TimeUnit;

import com.purcotton.netty.heartbeat.simple.client.handler.HeartBeatClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
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
        ChannelFuture future = null;
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
                    // 因为服务器端是5秒检测读超时，所以客户端必须在5秒内发送一次心跳，告之服务端，所以我们设置4秒
                    ch.pipeline().addLast("ping", new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));

                    ch.pipeline().addLast("decoder", new StringDecoder());
                    ch.pipeline().addLast("encoder", new StringEncoder());
                    // 客户端具体处理器
                    ch.pipeline().addLast(new HeartBeatClientHandler());
                }
            });

            future = client.connect(host, port).sync();
            future.channel().writeAndFlush("Hello Netty Server ,I am a common client");
            future.channel().closeFuture().sync();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            // group.shutdownGracefully();

            // 模拟简单重连
            if (null != future)
            {
                if (future.channel() != null && future.channel().isOpen())
                {
                    future.channel().close();
                }
            }
            System.out.println("准备重连");
            start();
            System.out.println("重连成功");
        }
    }

    public static void main(String[] args)
    {
        Client client = new Client("localhost", 8888);
        client.start();
    }
}
