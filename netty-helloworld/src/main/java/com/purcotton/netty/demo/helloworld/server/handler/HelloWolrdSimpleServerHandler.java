/*
 * Winner 
 * 文件名  :HelloWolrdSimpleChannelHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.demo.helloworld.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * [简要描述]:<br/>
 * [详细描述]:channelRead0还有一个好处就是你不用关心释放资源，因为源码中已经帮你释放了<br/>
 * SimpleChannelInboundHandler指定了一个泛型，需要与绑定的Decoder保持一直，否则不会正常收取到消息<br>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
public class HelloWolrdSimpleServerHandler extends SimpleChannelInboundHandler<String>
{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception
    {
        System.out.println("SimpleChannelInboundHandler channelRead0....");
        System.out.println(ctx.channel().remoteAddress() + "->Server :" + msg);
        ctx.write("server write:" + msg);
        ctx.flush();

    }

}
