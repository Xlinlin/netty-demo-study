/*
 * Winner 
 * 文件名  :MessageDecoder.java
 * 创建人  :llxiao
 * 创建时间:2018年4月26日
*/

package com.xiao.netty.demo.protocal.stack.coder.marshalling;

import java.util.HashMap;
import java.util.Map;

import com.xiao.netty.demo.protocal.stack.message.marshalling.MessageHeader;
import com.xiao.netty.demo.protocal.stack.message.marshalling.StackMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月26日
 * @since JDK 1.8
 */
public class MessageDecoder extends LengthFieldBasedFrameDecoder
{
    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength)
    {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    private MarshallingDecoder decoder;

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception
    {
        StackMessage message = new StackMessage();
        MessageHeader header = new MessageHeader();

        header.setMagicCode(in.readInt());
        header.setLength(in.readInt());
        header.setSessionId(in.readLong());
        header.setType(in.readByte());
        header.setPriority(in.readByte());

        // 消息头
        setAttachment(header, in);
        message.setHeader(header);

        // ????
        if (in.readableBytes() > 4)
        {
            // 设置消息体
            message.setBody(decoder.decode(in));
        }

        return message;
    }

    /**
     * [简要描述]:消息附件设置<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param header
     * @param in
     * @throws Exception
     */
    private void setAttachment(MessageHeader header, ByteBuf in) throws Exception
    {
        // 附件消息长度
        int size = in.readInt();
        if (size > 0)
        {
            Map<String, Object> attch = new HashMap<String, Object>(size);
            int keySize = 0;
            byte[] keyArray = null;
            String key = null;
            Object val = null;
            for (int i = 0; i < size; i++)
            {
                // 写入key的长度
                keySize = in.readInt();
                keyArray = new byte[keySize];
                // 读取key的byte
                in.readBytes(keyArray);
                key = new String(keyArray, CharsetUtil.UTF_8);

                // 获取值
                val = decoder.decode(in);
                attch.put(key, val);
            }
            // 消息头添加附件信息
            header.setAttachment(attch);
        }

    }

}
