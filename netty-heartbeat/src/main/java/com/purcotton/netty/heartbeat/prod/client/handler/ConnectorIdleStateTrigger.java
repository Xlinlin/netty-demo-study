/*
 * Winner 
 * 文件名  :ConnectorIdleStateTrigger.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.heartbeat.prod.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * [简要描述]:心跳检测状态处理类<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
@Sharable
public class ConnectorIdleStateTrigger extends ChannelInboundHandlerAdapter
{
    /**
     * 持久化一个以utf-8的固定字符串 以ByteBuf的形式，内部会以这个字符串的大小为它创建固定的容器，以UTF-8 Charset 来编码
     */
    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
            .unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat", CharsetUtil.UTF_8));

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        if (evt instanceof IdleStateEvent)
        {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE)
            {
                // duplicate 复制当前对象，复制后的对象与前对象共享缓冲区，且维护自己的独立索引
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
            }
        }
        else
        {
            super.userEventTriggered(ctx, evt);
        }
    }
}
