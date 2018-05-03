/*
 * Winner 
 * 文件名  :MessageEncoder.java
 * 创建人  :llxiao
 * 创建时间:2018年4月26日
*/

package com.xiao.netty.demo.protocal.stack.coder.marshalling;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xiao.netty.demo.protocal.stack.message.marshalling.StackMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月26日
 * @since JDK 1.8
 */
public class MessageEncoder extends MessageToMessageEncoder<StackMessage>
{

    private MarshallingEncoder marshallingEncoder;

    public MessageEncoder() throws Exception
    {
        this.marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, StackMessage msg, List<Object> out) throws Exception
    {
        if (null == msg || null == msg.getHeader())
        {
            throw new Exception("The encode message is null");
        }

        ByteBuf sendBuf = Unpooled.buffer();
        // 协议标识
        sendBuf.writeInt(msg.getHeader().getMagicCode());
        // 消息长度
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionId());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());

        // 写入附件信息
        setAttachment(msg, sendBuf);

        if (null != msg.getBody())
        {
            // 写入消息体
            marshallingEncoder.encode(msg.getBody(), sendBuf);
        }
        else
        {
            // 心跳应答消息
            sendBuf.writeInt(0);
        }
        // 在此缓冲区中指定的绝对索引处设置指定的32位整数，更新length位置的值
        sendBuf.setInt(4, sendBuf.readableBytes());

    }

    /**
     * [简要描述]:附件信息<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param msg
     * @param sendBuf
     * @throws Exception
     */
    private void setAttachment(StackMessage msg, ByteBuf sendBuf) throws Exception
    {
        Map<String, Object> attachment = msg.getHeader().getAttachment();
        if (attachment != null && attachment.size() > 0)
        {
            sendBuf.writeInt(attachment.size());
            // 遍历所有附件信息
            Iterator<Entry<String, Object>> iter = attachment.entrySet().iterator();
            Entry<String, Object> entry = null;
            while (iter.hasNext())
            {
                entry = iter.next();

                // 写入key
                byte[] keyArray = entry.getKey().getBytes(CharsetUtil.UTF_8);
                sendBuf.writeInt(keyArray.length);
                sendBuf.writeBytes(keyArray);

                // 通过marshalling写入value
                marshallingEncoder.encode(entry.getValue(), sendBuf);
            }
        }
        else
        {
            // 无附件，写入0
            sendBuf.writeInt(0);
        }

    }

}
