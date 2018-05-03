/*
 * Winner 
 * 文件名  :ProtobufTest.java
 * 创建人  :llxiao
 * 创建时间:2018年4月24日
*/

package com.purcotton.netty.protocol.protobuf.main;

import org.junit.Test;

import com.xiao.netty.demo.common.client.Client;
import com.xiao.netty.demo.common.server.Server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月24日
 * @since JDK 1.8
 */
public class ProtobufTest
{
    private int port = 9000;
    private String host = "127.0.0.1";

    @Test
    public void testServer()
    {
        ChannelHandler handler = new ProtoBufServerHandler();
        ChannelInitializer<SocketChannel> initializer = new ProtobufChannelInitializer(handler);
        Server server = new Server(port, initializer);
        server.start();
    }

    @Test
    public void testClient()
    {
        ChannelHandler handler = new ProtoBufClientHandler();
        ChannelInitializer<SocketChannel> initializer = new ProtobufChannelInitializer(handler);
        Client client = new Client(host, port, initializer);
        client.start();
    }
}
