/*
 * Winner 
 * 文件名  :RpcDecoder.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.common.coder;

import java.util.List;

import com.xiao.netty.rpc.common.util.ProtostuffUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * [简要描述]:Netty解码器<br/>
 * [详细描述]:probuffer对byte进行反序列化成class<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public class RpcDecoder extends ByteToMessageDecoder
{
    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass)
    {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        if (in.readableBytes() < 4)
        {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0)
        {
            ctx.close();
        }
        if (in.readableBytes() < dataLength)
        {
            in.resetReaderIndex();
        }
        // 将ByteBuf转换为byte[]
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        // 将data转换成object
        Object obj = ProtostuffUtil.deserialize(data, genericClass);
        out.add(obj);
    }

}
