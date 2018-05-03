/*
 * Winner 
 * 文件名  :ClientHeartBeatThread.java
 * 创建人  :llxiao
 * 创建时间:2018年4月28日
*/

package com.xiao.netty.demo.protocal.stack.handler.protobuf;

import com.xiao.netty.demo.protocal.stack.message.MessageConstants;
import com.xiao.netty.demo.protocal.stack.message.proto.NettyMessageProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * [简要描述]:客户端心跳线程<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
@Sharable
public class ClientProtoHeartBeatThread implements Runnable
{

    private ChannelHandlerContext heartBeatContext;

    public ClientProtoHeartBeatThread(ChannelHandlerContext heartBeatContext)
    {
        this.heartBeatContext = heartBeatContext;
    }

    @Override
    public void run()
    {
        NettyMessageProto.NettyMessage heartBeat = buildHeartBeat();
        heartBeatContext.writeAndFlush(heartBeat);
    }

    /**
     * [简要描述]:构建心跳消息，无消息体<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @return
     */
    private NettyMessageProto.NettyMessage buildHeartBeat()
    {
        NettyMessageProto.NettyMessage.Builder builder = NettyMessageProto.NettyMessage.newBuilder();
        builder.setMagic(MessageConstants.MAC_NUM);
        builder.setType(NettyMessageProto.NettyMessage.MessageType.PING);
        return builder.build();
    }

}
