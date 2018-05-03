/*
 * Winner 
 * 文件名  :ChannelHandlerHolder.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.heartbeat.prod.client.handler;

import io.netty.channel.ChannelHandler;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 * 客户端的ChannelHandler集合，由子类实现，这样做的好处：
 * 继承这个接口的所有子类可以很方便地获取ChannelPipeline中的Handlers
 * 获取到handlers之后方便ChannelPipeline中的handler的初始化和在重连的时候也能很方便
 * 地获取所有的handlers
 * 
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
public interface ChannelHandlerHolder
{
    ChannelHandler[] handlers();
}
