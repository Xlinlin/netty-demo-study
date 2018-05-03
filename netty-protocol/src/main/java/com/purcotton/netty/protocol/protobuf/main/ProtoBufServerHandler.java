/*
 * Winner 
 * 文件名  :ProtoBufServerHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.protocol.protobuf.main;

import java.util.List;

import com.purcotton.netty.protocol.protobuf.bean.RichManProto;
import com.purcotton.netty.protocol.protobuf.bean.RichManProto.RichMan.Car;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * [简要描述]:protobuf 接受消息处理<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
public class ProtoBufServerHandler extends SimpleChannelInboundHandler<RichManProto.RichMan>
{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RichManProto.RichMan msg) throws Exception
    {
        System.out.println(msg.toString());
        System.out.println(msg.getName() + "他有" + msg.getCarsCount() + "量车");
        List<Car> lists = msg.getCarsList();
        if (null != lists)
        {
            for (Car car : lists)
            {
                System.out.println(car.getName());
            }
        }
        RichManProto.RichMan.Builder builder = msg.toBuilder();
        builder.setName("李嘉诚");
        builder.setId(2);
        ctx.channel().write(builder.build());
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }

}
