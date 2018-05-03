/*
 * Winner 
 * 文件名  :BaseServerHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.demo.stick.server.handler;

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
public class BaseServerHandler extends ChannelInboundHandlerAdapter
{
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {

        String body = (String) msg;
        System.out.println("server receive order : " + body + ";the counter is: " + ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        ctx.close();
    }
}
