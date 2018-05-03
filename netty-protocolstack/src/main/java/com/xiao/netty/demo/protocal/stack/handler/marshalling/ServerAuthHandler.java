/*
 * Winner 
 * 文件名  :AuthenticationHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月28日
*/

package com.xiao.netty.demo.protocal.stack.handler.marshalling;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xiao.netty.demo.protocal.stack.message.marshalling.MessageHeader;
import com.xiao.netty.demo.protocal.stack.message.marshalling.MessageType;
import com.xiao.netty.demo.protocal.stack.message.marshalling.StackMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * [简要描述]:鉴权<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
public class ServerAuthHandler extends SimpleChannelInboundHandler<StackMessage>
{
    // 已存在的节点
    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();

    // ip白名单
    private String[] whitekList =
    {
            "127.0.0.1", "172.16.80.194"
    };

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StackMessage msg) throws Exception
    {
        System.out.println("Server auth handler recive client msg:" + msg);
        MessageHeader header = msg.getHeader();
        // 仅处理捂手消息
        if (null != header && header.getType() == MessageType.SHAKE_HANDS_REQ.getType())
        {
            System.out.println("Client auth......");
            StackMessage response = null;

            String nodeIndex = ctx.channel().remoteAddress().toString();
            // 拒绝重连
            if (nodeCheck.containsKey(nodeIndex))
            {
                System.out.println("Can't reconnection!");
                response = buildResponse(MessageType.SHAKE_HANDS_FAILD);
            }
            else
            {
                // 客户端ip地址
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                // 简单的黑白名单校验
                for (String string : whitekList)
                {
                    if (string.equals(ip))
                    {
                        isOK = true;
                        break;
                    }
                }
                if (isOK)
                {
                    nodeCheck.put(nodeIndex, true);
                    response = buildResponse(MessageType.SHAKE_HANDS_SUCCESS);
                    System.out.println("Client auth ok!");
                }
                else
                {
                    System.out.println("Illegal customers!");
                    response = buildResponse(MessageType.SHAKE_HANDS_FAILD);
                }
            }
            ctx.writeAndFlush(response);
        }
        else
        {
            // 出发消息事件
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        System.out.println("Client connection error and remove client:" + ctx.channel().remoteAddress().toString());
        nodeCheck.remove(ctx.channel().remoteAddress().toString());// 删除缓存
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    /**
     * [简要描述]:握手响应报文<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param shakeHandsStatus
     *            返回状态
     * @return
     */
    private StackMessage buildResponse(MessageType shakeHandsStatus)
    {
        // 仅响应握手状态即可
        StackMessage message = new StackMessage();
        MessageHeader header = new MessageHeader();
        header.setType(MessageType.SHAKE_HANDS_RESP.getType());
        message.setBody(shakeHandsStatus);
        message.setHeader(header);
        return message;
    }
}
