/*
 * Winner 
 * 文件名  :Server.java
 * 创建人  :llxiao
 * 创建时间:2018年4月28日
*/

package com.xiao.netty.demo.protocal.stack.main;

import com.xiao.netty.demo.common.NettyConstant;
import com.xiao.netty.demo.common.basic.NettyConnection;
import com.xiao.netty.demo.common.server.Server;
import com.xiao.netty.demo.protocal.stack.handler.common.MyReadTimeoutHandler;
import com.xiao.netty.demo.protocal.stack.handler.protobuf.ServerProtoHeartBeatHandler;
import com.xiao.netty.demo.protocal.stack.handler.protobuf.ServerProtoLoginHandler;
import com.xiao.netty.demo.protocal.stack.initializer.ProtobufChannelInitializer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
public class ServerBootstrap
{
    public static void main(String[] args)
    {
        ChannelInitializer<SocketChannel> initializer = protobufInitializer();
        NettyConnection connection = new Server(NettyConstant.PORT, initializer);
        connection.start();
    }

    // private static ChannelInitializer<SocketChannel> marshallingInitializer()
    // {
    // return new ServerChannelInitializer();
    // }

    private static ChannelInitializer<SocketChannel> protobufInitializer()
    {
        ChannelHandler[] handler = new ChannelHandler[3];
        // read timeout 读超时 默认50S
        handler[0] = new MyReadTimeoutHandler(50);
        handler[1] = new ServerProtoLoginHandler();
        handler[2] = new ServerProtoHeartBeatHandler();
        return new ProtobufChannelInitializer(handler);
    }
}
