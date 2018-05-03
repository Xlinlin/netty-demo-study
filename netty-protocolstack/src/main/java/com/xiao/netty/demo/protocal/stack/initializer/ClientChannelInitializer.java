/*
 * Winner 
 * 文件名  :ClientChannelInitializer.java
 * 创建人  :llxiao
 * 创建时间:2018年4月28日
*/

package com.xiao.netty.demo.protocal.stack.initializer;

import com.xiao.netty.demo.protocal.stack.coder.msgpack.MsgpackDecode;
import com.xiao.netty.demo.protocal.stack.coder.msgpack.MsgpackEncoder;
import com.xiao.netty.demo.protocal.stack.handler.marshalling.ClientAuthHandler;
import com.xiao.netty.demo.protocal.stack.handler.marshalling.ClientHeartBeatHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * [简要描述]:初始连接<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel>
{

    @Override
    protected void initChannel(SocketChannel ch) throws Exception
    {
        // DECODER 解码限制消息大小
        // ch.pipeline().addLast("decoder", new MessageDecoder(1024 * 1024, 4, 4));
        ch.pipeline().addLast("decoder", new MsgpackDecode());

        // ENCODER 编码
        // ch.pipeline().addLast("encoder", new MessageEncoder());
        ch.pipeline().addLast("encoder", new MsgpackEncoder());

        // read timeout 读超时 默认50S
        ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));

        // author 握手请求
        ch.pipeline().addLast("author", new ClientAuthHandler());

        // heartbeat 心跳
        ch.pipeline().addLast("heartbeat", new ClientHeartBeatHandler());

    }

}
