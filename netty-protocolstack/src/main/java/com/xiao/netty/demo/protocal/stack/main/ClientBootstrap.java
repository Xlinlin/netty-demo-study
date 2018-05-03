/*
 * Winner 
 * 文件名  :Client.java
 * 创建人  :llxiao
 * 创建时间:2018年4月28日
*/

package com.xiao.netty.demo.protocal.stack.main;

import com.xiao.netty.demo.common.NettyConstant;
import com.xiao.netty.demo.common.client.Client;
import com.xiao.netty.demo.common.client.ReconnectionHandler;
import com.xiao.netty.demo.protocal.stack.handler.ClientReconnectionHandler;
import com.xiao.netty.demo.protocal.stack.handler.common.MyReadTimeoutHandler;
import com.xiao.netty.demo.protocal.stack.handler.protobuf.ClientProtoHeartBeatHandler;
import com.xiao.netty.demo.protocal.stack.handler.protobuf.ClientProtoLoginHandler;
import com.xiao.netty.demo.protocal.stack.initializer.ProtobufChannelInitializer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * [简要描述]:客户端启动类<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
public class ClientBootstrap
{
    public static void main(String[] args)
    {
        ChannelInitializer<SocketChannel> initializer = protobufInitializer();
        Client client = new Client(NettyConstant.REMOTEIP, NettyConstant.PORT, initializer);
        ReconnectionHandler reconnection = new ClientReconnectionHandler(client);
        client.setReconnectHandler(reconnection);
        client.start();
    }

    // private static ChannelInitializer<SocketChannel> marshallingInitializer()
    // {
    // return new ClientChannelInitializer();
    // }

    private static ChannelInitializer<SocketChannel> protobufInitializer()
    {
        ChannelHandler[] handler = new ChannelHandler[3];
        // read timeout 读超时 默认50S
        handler[0] = new MyReadTimeoutHandler(50);
        // 握手消息
        handler[1] = new ClientProtoLoginHandler();
        // 心跳消息
        handler[2] = new ClientProtoHeartBeatHandler();
        return new ProtobufChannelInitializer(handler);
    }
}
