/*
 * Winner 
 * 文件名  :ClientServiceHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月24日
*/

package com.purcotton.netty.protocol.messagepack.main;

import com.purcotton.netty.common.entity.UserInfo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月24日
 * @since JDK 1.8
 */
public class ClientServiceHandler extends ChannelInboundHandlerAdapter
{
    private int num;

    public ClientServiceHandler(int num)
    {
        this.num = num;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("建立连接...............");
        UserInfo[] infos = getUserInfo();
        for (UserInfo userInfo : infos)
        {
            // System.out.println(userInfo.toString());
            ctx.writeAndFlush(userInfo);
        }
        System.out.println("发送所有消息成功！消息个数：" + infos.length);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("Recive message : " + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }

    private UserInfo[] getUserInfo()
    {
        UserInfo[] infos = new UserInfo[num];
        UserInfo info = null;
        for (int i = 0; i < num; i++)
        {
            info = new UserInfo();
            info.setAge(i);
            info.setName("ABCDEF-->" + i);
            infos[i] = info;
        }
        return infos;
    }

}
