/*
 * Winner 
 * 文件名  :ProtoBufClientHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.protocol.protobuf.main;

import java.util.ArrayList;
import java.util.List;

import com.purcotton.netty.protocol.protobuf.bean.RichManProto;
import com.purcotton.netty.protocol.protobuf.bean.RichManProto.RichMan;
import com.purcotton.netty.protocol.protobuf.bean.RichManProto.RichMan.Car;
import com.purcotton.netty.protocol.protobuf.bean.RichManProto.RichMan.CarType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
public class ProtoBufClientHandler extends SimpleChannelInboundHandler<RichManProto.RichMan>
{

    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        System.out.println("=======================================");
        RichManProto.RichMan.Builder builder = RichManProto.RichMan.newBuilder();
        builder.setName("王思聪");
        builder.setId(1);
        builder.setEmail("wsc@163.com");

        List<RichManProto.RichMan.Car> cars = new ArrayList<RichManProto.RichMan.Car>();
        Car car1 = RichManProto.RichMan.Car.newBuilder().setName("上海大众超跑").setType(CarType.DASAUTO).build();
        Car car2 = RichManProto.RichMan.Car.newBuilder().setName("Aventador").setType(CarType.LAMBORGHINI).build();
        Car car3 = RichManProto.RichMan.Car.newBuilder().setName("奔驰SLS级AMG").setType(CarType.BENZ).build();

        cars.add(car1);
        cars.add(car2);
        cars.add(car3);

        builder.addAllCars(cars);
        ctx.writeAndFlush(builder.build());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RichMan msg) throws Exception
    {
        System.out.println("Recive server message:");
        System.out.println(msg.getName() + ' ' + msg.getId());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }

}
