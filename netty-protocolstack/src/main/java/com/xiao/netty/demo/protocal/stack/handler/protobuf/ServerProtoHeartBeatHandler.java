/*
 * Winner 
 * 文件名  :ServerHeartBeatHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月28日
*/

package com.xiao.netty.demo.protocal.stack.handler.protobuf;

import com.xiao.netty.demo.protocal.stack.message.MessageConstants;
import com.xiao.netty.demo.protocal.stack.message.proto.NettyMessageProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * [简要描述]:服务端心跳应答消息<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
@Sharable
public class ServerProtoHeartBeatHandler extends SimpleChannelInboundHandler<NettyMessageProto.NettyMessage>
{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessageProto.NettyMessage msg) throws Exception
    {
        // 客户端心跳消息
        if (msg.getType() == NettyMessageProto.NettyMessage.MessageType.PING)
        {
            System.out.println("=======>Client PING msg!");
            ctx.writeAndFlush(buildPong());
        }
        else
        {
            // 其他消息透传
            ctx.fireChannelRead(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        // 异常消息处理
        // cause.printStackTrace();
        ctx.fireExceptionCaught(cause);
    }

    /**
     * [简要描述]:构建服务端心跳PONG消息<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @return
     */
    private NettyMessageProto.NettyMessage buildPong()
    {
        NettyMessageProto.NettyMessage.Builder pongBuild = NettyMessageProto.NettyMessage.newBuilder();
        pongBuild.setType(NettyMessageProto.NettyMessage.MessageType.PONG);
        pongBuild.setMagic(MessageConstants.MAC_NUM);
        return pongBuild.build();
    }

}
