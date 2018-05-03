/*
 * Winner 
 * 文件名  :CustomDecoder.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.protocol.privates.common;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
public class CustomDecoder extends LengthFieldBasedFrameDecoder
{

    // 判断传送客户端传送过来的数据是否按照协议传输，头部信息的大小应该是 byte+byte+int = 1+1+4 = 6
    private static final int HEADER_SIZE = 6;

    private byte type;

    private byte flag;

    private int length;

    private String body;

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @author llxiao
     * @param maxFrameLength
     *            解码时，处理每个帧数据的最大长度
     * @param lengthFieldOffset
     *            该帧数据中，存放该帧数据的长度的数据的起始位置
     * @param lengthFieldLength
     *            记录该帧数据长度的字段本身的长度
     * @param lengthAdjustment
     *            修改帧数据长度字段中定义的值，可以为负数
     * @param initialBytesToStrip
     *            解析的时候需要跳过的字节数
     * @param failFast
     *            为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异常
     */
    public CustomDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
            int initialBytesToStrip, boolean failFast)
    {
        super(ByteOrder.BIG_ENDIAN, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment,
                initialBytesToStrip, failFast);
    }

    /**
     * [简要描述]:编码<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param ctx
     * @param in
     * @return
     * @throws Exception
     * @see io.netty.handler.codec.LengthFieldBasedFrameDecoder#decode(io.netty.channel.ChannelHandlerContext,
     *      io.netty.buffer.ByteBuf)
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception
    {
        if (null == in)
        {
            return null;
        }

        if (in.readableBytes() < HEADER_SIZE)
        {
            throw new Exception("可读信息段比头部信息都小，你在逗我？");
        }

        // 注意在读的过程中，readIndex的指针也在移动
        // 获得类型
        type = in.readByte();
        // 获得消息类型
        flag = in.readByte();
        // 获得消息体长度
        length = in.readInt();

        if (in.readableBytes() < length)
        {
            throw new Exception("head字段你告诉我长度是:" + length + ",但是真实情况是没有这么多，你又逗我？");
        }

        ByteBuf buf = in.readBytes(length);
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        body = new String(req, "UTF-8");

        CustomMsg customMsg = new CustomMsg(type, flag, length, body);
        return customMsg;
    }
}
