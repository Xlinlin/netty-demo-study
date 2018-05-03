/*
 * Winner 
 * 文件名  :ProtobufChannelInitializer.java
 * 创建人  :llxiao
 * 创建时间:2018年4月24日
*/

package com.xiao.netty.demo.protocal.stack.initializer;

import com.xiao.netty.demo.protocal.stack.message.proto.NettyMessageProto;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月24日
 * @since JDK 1.8
 */
public class ProtobufChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private ChannelHandler[] handler;

    public ProtobufChannelInitializer(ChannelHandler[] handler)
    {
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception
    {
        // protobuf协议 netty支持

        // 编码部分
        /*
         * 1.ProtobufDecoder仅负责解码，不支持读半包，因此需要再此之前一定要有能处理读半包的解码器，三种方式<br>
         * 1)netty自身提供的 ProtobufVarint32FrameDecoder<br>
         * 2)继承netty提供的通用半包解码器LengthFieldBasedFrameDecoder<br>
         * 3)继承ByteToMessageDecoder类，自己处理半包消息<br>
         * 如果仅使用ProtobufDecoder解码不处理读半包问题，是无法运行的
         */
        // 针对protobuf协议的ProtobufVarint32LengthFieldPrepender()所加的长度属性的解码器，解决读半包问题
        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
        // 将message转化成我们自定义的Rimanproto
        ch.pipeline().addLast(new ProtobufDecoder(NettyMessageProto.NettyMessage.getDefaultInstance()));

        // 解码 部分
        // 对protobuf协议的的消息头上加上一个长度为32的整形字段，用于标志这个消息的长度
        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
        ch.pipeline().addLast(new ProtobufEncoder());

        // 业务处理
        for (int i = 0; i < handler.length; i++)
        {
            ch.pipeline().addLast(handler[i]);
        }
    }
}
