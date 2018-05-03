/*
 * Winner 
 * 文件名  :HelloWorldClientHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.demo.helloworld.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * [简要描述]:ChannelInboundHandler处理输入的事件，事件由外部事件源产生，例如从一个socket接收到数据<br/>
 * [详细描述]:它的作用是将operation转到ChannelPipeline中的下一个ChannelHandler。子类可以重写一个方法的实现来改变。<br/>
 * 注意：在方法#channelRead(ChannelHandlerContext,Object)自动返回前，message不会释放。<br/>
 * 若需要一个可以自动释放接收消息的ChannelInboundHandler实现时，请考虑SimpleChannelInboundHandler。<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
public class HelloWorldClientHandler extends ChannelInboundHandlerAdapter
{
    /**
     * [简要描述]:io.netty.channel.ChannelInboundInvoker.fireChannelActive()<br/>
     * [详细描述]:激活，即表示已经连接成功<br/>
     * 
     * @author llxiao
     * @param ctx
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("Client --- Connect success");
        System.out.println("HelloWorldClientHandler Active：" + ctx.channel().id());
        // 绑定多个channelHandler时 如果需要都激活channelactive方法需要调用此代码代码逻辑
        // ctx.fireChannelActive();
    }

    /**
     * [简要描述]:io.netty.channel.ChannelInboundInvoker.fireChannelRead(Object)<br/>
     * [详细描述]:接受消息，消息到达时<br/>
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
        System.out.println("[ChannelInboundHandlerAdapter]Client --- Recive message");
        System.out.println("HelloWorldClientHandler recive Message:" + msg);
    }

    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        System.out.println("Client --- connect exception!");
        cause.printStackTrace();
        ctx.close();
    }
}
