package com.xiao.netty.demo.protocal.stack.initializer;

import com.xiao.netty.demo.protocal.stack.coder.msgpack.MsgpackDecode;
import com.xiao.netty.demo.protocal.stack.coder.msgpack.MsgpackEncoder;
import com.xiao.netty.demo.protocal.stack.handler.marshalling.ServerAuthHandler;
import com.xiao.netty.demo.protocal.stack.handler.marshalling.ServerHeartBeatHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * [简要描述]:服务端设置<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel>
{

    @Override
    protected void initChannel(SocketChannel ch) throws Exception
    {
        // Decoder
        // ch.pipeline().addLast("Decoder", new MessageDecoder(1024 * 1024, 4, 4));
        ch.pipeline().addLast("decoder", new MsgpackDecode());

        // Encoder
        // ch.pipeline().addLast("Encoder", new MessageEncoder());
        ch.pipeline().addLast("encoder", new MsgpackEncoder());

        // 读超时，默认50S
        ch.pipeline().addLast("readTimeout", new ReadTimeoutHandler(50));

        // auth
        ch.pipeline().addLast("auth", new ServerAuthHandler());

        // heatbeat
        ch.pipeline().addLast("heatbeat", new ServerHeartBeatHandler());
    }

}
