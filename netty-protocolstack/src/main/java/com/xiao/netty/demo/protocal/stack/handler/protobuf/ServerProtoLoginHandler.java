/*
 * Winner 
 * 文件名  :AuthenticationHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月28日
*/

package com.xiao.netty.demo.protocal.stack.handler.protobuf;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xiao.netty.demo.protocal.stack.message.MessageConstants;
import com.xiao.netty.demo.protocal.stack.message.proto.NettyMessageProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * [简要描述]:鉴权<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
@Sharable
public class ServerProtoLoginHandler extends SimpleChannelInboundHandler<NettyMessageProto.NettyMessage>
{
    // 已存在的节点
    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();

    // ip白名单
    private String[] whitekList =
    {
            "127.0.0.1", "172.16.80.194"
    };

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessageProto.NettyMessage msg) throws Exception
    {
        //System.out.println("Server auth handler recive client msg:" + msg);
       
        // 仅处理握手消息
        if (msg.getType() == NettyMessageProto.NettyMessage.MessageType.SHAKE_HANDS_REQ)
        {
            System.out.println("Client auth......");
            NettyMessageProto.NettyMessage response = null;

            String nodeIndex = ctx.channel().remoteAddress().toString();
            // 拒绝重连
            if (nodeCheck.containsKey(nodeIndex))
            {
                System.out.println("Can't reconnection!");
                response = buildResponse(MessageConstants.SHAKE_HANDS_FAILD);
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
                    response = buildResponse(MessageConstants.SHAKE_HANDS_SUCCESS);
                    System.out.println("Client auth ok!");
                }
                else
                {
                    System.out.println("Illegal customers!");
                    response = buildResponse(MessageConstants.SHAKE_HANDS_FAILD);
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
        //cause.printStackTrace();
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
    private NettyMessageProto.NettyMessage buildResponse(String shakeHandsStatus)
    {
        NettyMessageProto.NettyMessage.Builder build = NettyMessageProto.NettyMessage.newBuilder();
        build.setMagic(MessageConstants.MAC_NUM);
        build.setType(NettyMessageProto.NettyMessage.MessageType.SHAKE_HANDS_RESP);
        build.setContent(shakeHandsStatus);
        return build.build();
    }
}
