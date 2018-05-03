/*
 * Winner 
 * 文件名  :WebsocketHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月25日
*/

package com.xiao.netty.demo.websocket;

import java.time.LocalDateTime;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月25日
 * @since JDK 1.8
 */
@Sharable
public class WebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>
{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception
    {
        System.out.println("收到消息：" + msg.text());
        /**
         * writeAndFlush接收的参数类型是Object类型，但是一般我们都是要传入管道中传输数据的类型，比如我们当前的demo
         * 传输的就是TextWebSocketFrame类型的数据
         */
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务时间：" + LocalDateTime.now()));

    }

    /**
     * channel 通道 action 活跃的 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        // 添加
        System.out.println("客户端与服务端连接开启(channelActive)：" + ctx.channel().remoteAddress().toString());
    }

    /**
     * channel 通道 Inactive 不活跃的 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        // 移除
        System.out.println("客户端与服务端连接关闭(channelInactive)：" + ctx.channel().remoteAddress().toString());
    }

    /**
     * channel 通道 Read 读取 Complete 完成 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 ctx.flush()
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        ctx.flush();
    }

    /**
     * [简要描述]:每个channel都有一个唯一的id值<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param ctx
     * @throws Exception
     * @see io.netty.channel.ChannelHandlerAdapter#handlerAdded(io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        // 打印出channel唯一值，asLongText方法是channel的id的全名
        System.out.println("客户端建立连接(handlerAdded)，ID：" + ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("handlerRemoved：" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        System.out.println("异常发生");
        cause.printStackTrace();
        ctx.close();
    }
}
