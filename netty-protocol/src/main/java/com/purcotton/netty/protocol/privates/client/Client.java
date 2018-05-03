/*
 * Winner 
 * 文件名  :Client.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.protocol.privates.client;

import com.purcotton.netty.protocol.privates.client.handler.BaseClientHandler;
import com.purcotton.netty.protocol.privates.common.CustomDecoder;
import com.purcotton.netty.protocol.privates.common.CustomEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
    private static final int MAX_FRAME_LENGTH = 1024 * 1024;
    // 指的就是我们这边CustomMsg中length这个属性的大小，我们这边是int型，所以是4
    private static final int LENGTH_FIELD_LENGTH = 4;
    // 指的就是我们这边length字段的起始位置，因为前面有type和flag两个属性，且这两个属性都是byte，两个就是2字节，所以偏移量是2
    private static final int LENGTH_FIELD_OFFSET = 2;
    // 指的是length这个属性的值，假如我们的body长度是40，有时候，有些人喜欢将length写成44，因为length本身还占有4个字节，这样就需要调整一下，
    // 那么就需要-4，我们这边没有这样做，所以写0就可以了那么就需要-4，我们这边没有这样做，所以写0就可以了
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 0;

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
                    // 自定义协议解码器
                    ch.pipeline().addLast(new CustomDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH,
                            LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP, false));
                    // 自定义协议编码器
                    ch.pipeline().addLast(new CustomEncoder());
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
