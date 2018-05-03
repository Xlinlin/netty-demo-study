/*
 * Winner 
 * 文件名  :HelloWorldServerHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.demo.helloworld.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
public class HelloWorldServerHandler extends ChannelInboundHandlerAdapter
{
    /**
     * [简要描述]:接收消息，消息到达处理<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param ctx
     * @param msg
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext,
     *      java.lang.Object)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("ChannelInboundHandlerAdapter channelRead....");
        System.out.println(ctx.channel().remoteAddress() + "->Server :" + msg.toString());
        ctx.write("server write:" + msg);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        System.out.println("server exceptionCaught...");
        cause.printStackTrace();
        ctx.close();
    }
}
