/*
 * Winner 
 * 文件名  :BaseClientHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.demo.stick.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
public class BaseClientHandler extends ChannelInboundHandlerAdapter
{
    private byte[] req;

    private int counter;

    public BaseClientHandler()
    {
        // 2）单次发送的包内容过多的情况，粘包的现象：
        req = ("BazingaLyncc is learner" + System.getProperty("line.separator")).getBytes();

        // 1）单次发送的包内容过多的情况，拆包的现象：
        // req = longStr();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        ByteBuf message = null;
        // 2）单次发送的包内容过多的情况，粘包的现象：
        for (int i = 0; i < 100; i++)
        {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }

        // 1）单次发送的包内容过多的情况，拆包的现象：
        // message = Unpooled.buffer(req.length);
        // message.writeBytes(req);
        // ctx.writeAndFlush(message);
        // message = Unpooled.buffer(req.length);
        // message.writeBytes(req);
        // ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        String buf = (String) msg;
        System.out.println("Now is : " + buf + " ; the counter is : " + ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        ctx.close();
    }

    /**
     * [简要描述]:单次发送的包内容过多的情况，拆包的现象：<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @return
     */
    @SuppressWarnings("unused")
    private byte[] longStr()
    {
        String str = ("In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. His book w"
                + "ill give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the process "
                + "of configuring and connecting all of Netty’s components to bring your learned about threading models in ge"
                + "neral and Netty’s threading model in particular, whose performance and consistency advantages we discuss"
                + "ed in detail In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. Hi"
                + "s book will give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the"
                + " process of configuring and connecting all of Netty’s components to bring your learned about threading "
                + "models in general and Netty’s threading model in particular, whose performance and consistency advantag"
                + "es we discussed in detailIn this chapter you general, we recommend Java Concurrency in Practice by Bri"
                + "an Goetz. His book will give We’ve reached an exciting point—in the next chapter;the counter is: 1 2222"
                + "sdsa ddasd asdsadas dsadasdas");
        // TCP 拆包 增加一个标记 服务端使用LineBasedFrameDecoder解码，并且需要放在第一行位置
        str += System.getProperty("line.separator");
        return str.getBytes();
    }
}
