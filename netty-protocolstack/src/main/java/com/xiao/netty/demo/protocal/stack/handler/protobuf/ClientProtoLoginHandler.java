/*
 * Winner 
 * 文件名  :ClientAuthHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月28日
*/

package com.xiao.netty.demo.protocal.stack.handler.protobuf;

import com.xiao.netty.demo.protocal.stack.message.MessageConstants;
import com.xiao.netty.demo.protocal.stack.message.proto.NettyMessageProto;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * [简要描述]:客户端发起鉴权请求<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
@Sharable
public class ClientProtoLoginHandler extends SimpleChannelInboundHandler<NettyMessageProto.NettyMessage>
{

    /**
     * [简要描述]:客户端在建立连接的时候发情鉴权请求<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param ctx
     * @throws Exception
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("Client send shake hand reqeust!!!!!");
        // 发送握手请求
        ctx.writeAndFlush(builderShakeReq());
    }

    /**
     * [简要描述]:处理服务端响应握手应答消息<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param ctx
     * @param msg
     * @throws Exception
     * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext,
     *      java.lang.Object)
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessageProto.NettyMessage msg) throws Exception
    {
        // System.out.println("Client auth handler Recive message:" + msg);
        boolean success = true;
        if (msg.getType() == NettyMessageProto.NettyMessage.MessageType.SHAKE_HANDS_RESP)
        {
            String respMsg = msg.getContent();
            // 握手失败，关闭连接，或做相应的重连
            if (MessageConstants.SHAKE_HANDS_FAILD.equals(respMsg))
            {
                System.out.println("Client shake hand faild!");
                success = false;
            }
            System.out.println("Login Success!!");
        }
        if (success)
        {
            ctx.fireChannelRead(msg);
        }
        else
        {
            System.out.println("Login error!");
            ctx.close();
        }
    }

    /**
     * [简要描述]:连接异常处理<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param ctx
     * @param cause
     * @throws Exception
     * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext,
     *      java.lang.Throwable)
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        System.out.println("Auth connection error!");
        //cause.printStackTrace();
        // 触发后续异常
        ctx.fireExceptionCaught(cause);
    }

    /**
     * [简要描述]:构建握手请求消息<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @return
     */
    private NettyMessageProto.NettyMessage builderShakeReq()
    {

        NettyMessageProto.NettyMessage.Builder builder = NettyMessageProto.NettyMessage.newBuilder();
        builder.setMagic(MessageConstants.MAC_NUM);
        builder.setType(NettyMessageProto.NettyMessage.MessageType.SHAKE_HANDS_REQ);

        return builder.build();
    }

}
