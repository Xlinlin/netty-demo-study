/*
 * Winner 
 * 文件名  :BaseServerHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.protocol.privates.server.handler;

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
public class BaseServerHandler extends SimpleChannelInboundHandler<CustomMsg>
{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println(ctx.channel().id() + " connection success!!");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CustomMsg msg) throws Exception
    {
        System.out.println("Client->Server:" + ctx.channel().remoteAddress() + " send " + msg.getBody());
        msg.setBody("Hello client!!!!");
        ctx.channel().writeAndFlush(msg);
    }

}
