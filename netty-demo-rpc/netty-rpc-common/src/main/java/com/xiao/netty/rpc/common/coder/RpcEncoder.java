/*
 * Winner 
 * 文件名  :RpcEncoder.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.common.coder;

import com.xiao.netty.rpc.common.util.ProtostuffUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * [简要描述]:netty编码工具类<br/>
 * [详细描述]:基于probuffer将对象序列化Byte<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public class RpcEncoder extends MessageToByteEncoder<Object>
{
    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass)
    {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception
    {
        // 序列化
        if (genericClass.isInstance(msg))
        {
            byte[] data = ProtostuffUtil.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }

}
