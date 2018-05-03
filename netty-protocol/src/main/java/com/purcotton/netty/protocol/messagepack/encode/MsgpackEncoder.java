/*
 * Winner 
 * 文件名  :MsgpackEncoder.java
 * 创建人  :llxiao
 * 创建时间:2018年4月24日
*/

package com.purcotton.netty.protocol.messagepack.encode;

import org.msgpack.MessagePack;

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
public class MsgpackEncoder extends MessageToByteEncoder<Object>
{

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception
    {
        MessagePack msgPack = new MessagePack();
        byte[] msgByte = msgPack.write(msg);
        // 编码后写入bytebuf
        out.writeBytes(msgByte);
    }

}
