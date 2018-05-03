/*
 * Winner 
 * 文件名  :TestMain.java
 * 创建人  :llxiao
 * 创建时间:2018年4月24日
*/

package com.purcotton.netty.protocol.messagepack.main;

import org.junit.Test;

import com.purcotton.netty.protocol.messagepack.decode.MsgpackDecode;
import com.purcotton.netty.protocol.messagepack.encode.MsgpackEncoder;
import com.xiao.netty.demo.common.client.Client;
import com.xiao.netty.demo.common.server.Server;

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
public class MsgpcakTest
{

    private int port = 9000;
    private String host = "127.0.0.1";

    @Test
    public void testClient()
    {

        Client client = new Client(host, port, new ChannelInitializer<SocketChannel>()
        {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception
            {
                ch.pipeline().addLast("msgpack decoder", new MsgpackDecode());
                ch.pipeline().addLast("msgpack encode", new MsgpackEncoder());

                ch.pipeline().addLast(new ClientServiceHandler(50));
            }
        });
        client.start();
    }

    @Test
    public void testServer()
    {

        Server sever = new Server(port, new ChannelInitializer<SocketChannel>()
        {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception
            {
                ch.pipeline().addLast("msgpack decoder", new MsgpackDecode());
                ch.pipeline().addLast("msgpack encode", new MsgpackEncoder());

                ch.pipeline().addLast(new ServerServiceHanlder());

            }
        });
        sever.start();
    }

}
