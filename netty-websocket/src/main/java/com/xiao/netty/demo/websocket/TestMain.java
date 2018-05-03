/*
 * Winner 
 * 文件名  :TestMain.java
 * 创建人  :llxiao
 * 创建时间:2018年4月25日
*/

package com.xiao.netty.demo.websocket;

import org.junit.Test;

import com.xiao.netty.demo.common.server.Server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月25日
 * @since JDK 1.8
 */
public class TestMain
{
    @Test
    public void testWs()
    {
        ChannelHandler websocketChannel = new WebsocketHandler();
        ChannelInitializer<SocketChannel> initializer = new WebsocketChannelInitializer(websocketChannel);
        Server server = new Server(9000, initializer);
        server.start();
    }
}
