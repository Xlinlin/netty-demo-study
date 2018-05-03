/*
 * Winner 
 * 文件名  :HttpFileServerHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月25日
*/

package com.xiao.netty.demo.http.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AsciiString;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 * 声明泛型为<FullHttpRequest>，声明之后，只有msg为FullHttpRequest的消息才能进来<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月25日
 * @since JDK 1.8
 */
@Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception
    {
        System.out.println("class:" + msg.getClass().getName());
        // 生成response
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.wrappedBuffer("Hello netty http server!".getBytes()));

        HttpHeaders heads = response.headers();
        heads.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
        // 添加header描述length，告诉浏览器消息长度
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("channelReadComplete");
        super.channelReadComplete(ctx);
        // channel读取完成之后需要输出缓冲流,如果没有这一步，你会发现postman同样会一直在刷新。
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        System.out.println("exceptionCaught");
        if (null != cause)
        {
            cause.printStackTrace();
        }
        if (null != ctx)
        {
            ctx.close();
        }
    }

}
