/*
 * Winner 
 * 文件名  :MsgpackEncoder.java
 * 创建人  :llxiao
 * 创建时间:2018年4月24日
*/

package com.xiao.netty.demo.protocal.stack.coder.msgpack;

import org.msgpack.MessagePack;

import com.xiao.netty.demo.protocal.stack.message.marshalling.StackMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * [简要描述]:MessagePack 编码器<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月24日
 * @since JDK-1.8
 */
public class MsgpackEncoder extends MessageToByteEncoder<StackMessage>
{

    @Override
    protected void encode(ChannelHandlerContext ctx, StackMessage msg, ByteBuf out) throws Exception
    {
        MessagePack msgPack = new MessagePack();
        byte[] msgByte = msgPack.write(msg);
        // 编码后写入bytebuf
        out.writeBytes(msgByte);
    }

}
