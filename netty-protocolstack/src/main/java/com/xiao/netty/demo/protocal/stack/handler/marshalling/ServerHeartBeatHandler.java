/*
 * Winner 
 * 文件名  :ServerHeartBeatHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月28日
*/

package com.xiao.netty.demo.protocal.stack.handler.marshalling;

import com.xiao.netty.demo.protocal.stack.message.marshalling.MessageHeader;
import com.xiao.netty.demo.protocal.stack.message.marshalling.MessageType;
import com.xiao.netty.demo.protocal.stack.message.marshalling.StackMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * [简要描述]:服务端心跳应答消息<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
public class ServerHeartBeatHandler extends SimpleChannelInboundHandler<StackMessage>
{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StackMessage msg) throws Exception
    {
        MessageHeader header = msg.getHeader();
        // 客户端心跳消息
        if (null != header && header.getType() == MessageType.PING.getType())
        {
            System.out.println("=======>Client PING msg!");
            ctx.writeAndFlush(buildPong());
        }
        else
        {
            // 其他消息透传
            ctx.fireChannelRead(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        ctx.fireExceptionCaught(cause);
    }

    /**
     * [简要描述]:构建服务端心跳PONG消息<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @return
     */
    private Object buildPong()
    {
        StackMessage message = new StackMessage();
        MessageHeader header = new MessageHeader();
        header.setType(MessageType.PONG.getType());
        message.setHeader(header);
        return message;
    }

}
