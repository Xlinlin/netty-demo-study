/*
 * Winner 
 * 文件名  :AcceptorIdleStateTrigger.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.heartbeat.prod.server.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
@Sharable
public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter
{
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        if (evt instanceof IdleStateEvent)
        {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE)
            {
                throw new Exception("idle exception");
            }
        }
        else
        {
            super.userEventTriggered(ctx, evt);
        }
    }
}
