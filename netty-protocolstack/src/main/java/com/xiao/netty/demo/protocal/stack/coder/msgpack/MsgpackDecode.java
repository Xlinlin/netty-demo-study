/*
 * Winner 
 * 文件名  :MsgpackDecode.java
 * 创建人  :llxiao
 * 创建时间:2018年4月24日
*/

package com.xiao.netty.demo.protocal.stack.coder.msgpack;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * [简要描述]:MessagePack 解码器<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月24日
 * @since JDK-1.8
 */
public class MsgpackDecode extends MessageToMessageDecoder<ByteBuf>
{

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     * 1.从bytebuf中拿到Byte数组<br/>
     * 2.Messagepack反序列化<br/>
     * 3.添加到List中<br/>
     * 
     * @author llxiao
     * @param ctx
     * @param msg
     * @param out
     * @see io.netty.handler.codec.MessageToMessageDecoder#decode(io.netty.channel.ChannelHandlerContext,
     *      java.lang.Object, java.util.List)
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception
    {
        final byte[] array;
        final int length = msg.readableBytes();
        array = new byte[length];
        // 可读位置，目标数组，起始位置，结束位置
        msg.getBytes(msg.readerIndex(), array, 0, length);
        MessagePack message = new MessagePack();
        out.add(message.read(array));
    }

}
