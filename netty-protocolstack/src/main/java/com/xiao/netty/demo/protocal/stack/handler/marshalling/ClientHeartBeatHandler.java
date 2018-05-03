/*
 * Winner 
 * 文件名  :ClientHeartBeatHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月28日
*/

package com.xiao.netty.demo.protocal.stack.handler.marshalling;

import java.util.concurrent.TimeUnit;

import com.xiao.netty.demo.protocal.stack.message.marshalling.MessageHeader;
import com.xiao.netty.demo.protocal.stack.message.marshalling.MessageType;
import com.xiao.netty.demo.protocal.stack.message.marshalling.StackMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * [简要描述]:客户端心跳<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
public class ClientHeartBeatHandler extends SimpleChannelInboundHandler<StackMessage>
{
    /**
     * 定时心跳任务
     */
    private volatile ScheduledFuture<?> heartBeatTask;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StackMessage msg) throws Exception
    {
        System.out.println("HearBeat handler recive message.......");
        MessageHeader header = msg.getHeader();

        if (null != header && header.getType() == MessageType.SHAKE_HANDS_RESP.getType())
        {
            // 握手成功
            // 建立心跳 5S发送一次心跳
            heartBeatTask = ctx.executor().scheduleAtFixedRate(new ClientHeartBeatThread(ctx), 0, 5,
                    TimeUnit.MICROSECONDS);
        }
        else if (MessageType.PONG.getType() == header.getType())
        {
            // 服务端心跳回复消息
            System.out.println("Client recive heartbeat msg: PONG");
        }
        else
        {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * [简要描述]:出现异常，取消心跳任务<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param ctx
     * @param cause
     * @throws Exception
     * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext,
     *      java.lang.Throwable)
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        if (null != heartBeatTask)
        {
            heartBeatTask.cancel(true);
            heartBeatTask = null;
        }
        // 出发后续的exceptionCaught
        ctx.fireExceptionCaught(cause);
    }
}
