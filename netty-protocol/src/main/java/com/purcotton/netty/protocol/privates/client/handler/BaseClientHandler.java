/*
 * Winner 
 * 文件名  :BaseClientHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.protocol.privates.client.handler;

import com.purcotton.netty.protocol.privates.common.CustomMsg;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
public class BaseClientHandler extends SimpleChannelInboundHandler<CustomMsg>
{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println(ctx.channel().id() + " Connection success!");
        CustomMsg customMsg = new CustomMsg((byte) 0xAB, (byte) 0xCD, "Hello,Netty".length(), "Hello,Netty");
        ctx.writeAndFlush(customMsg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CustomMsg msg) throws Exception
    {
        System.out.println(ctx.channel().id() + " Recive server message:" + msg.getBody());
        System.out.println("message:" + msg);
    }

}
