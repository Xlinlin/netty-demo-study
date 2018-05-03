/*
 * Winner 
 * 文件名  :ClientHeartBeatThread.java
 * 创建人  :llxiao
 * 创建时间:2018年4月28日
*/

package com.xiao.netty.demo.protocal.stack.handler.marshalling;

import com.xiao.netty.demo.protocal.stack.message.marshalling.MessageHeader;
import com.xiao.netty.demo.protocal.stack.message.marshalling.MessageType;
import com.xiao.netty.demo.protocal.stack.message.marshalling.StackMessage;

import io.netty.channel.ChannelHandlerContext;

/**
 * [简要描述]:客户端心跳线程<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
public class ClientHeartBeatThread implements Runnable
{

    private ChannelHandlerContext heartBeatContext;

    public ClientHeartBeatThread(ChannelHandlerContext heartBeatContext)
    {
        this.heartBeatContext = heartBeatContext;
    }

    @Override
    public void run()
    {
        StackMessage heartBeatmsg = buildHeartBeat();
        heartBeatContext.writeAndFlush(heartBeatmsg);
    }

    /**
     * [简要描述]:构建心跳消息，无消息体<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @return
     */
    private StackMessage buildHeartBeat()
    {
        StackMessage heartBeat = new StackMessage();
        MessageHeader header = new MessageHeader();
        header.setType(MessageType.PING.getType());
        heartBeat.setHeader(header);
        return heartBeat;
    }

}
