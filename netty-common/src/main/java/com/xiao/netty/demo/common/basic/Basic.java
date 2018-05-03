/*
 * Winner 
 * 文件名  :Basic.java
 * 创建人  :llxiao
 * 创建时间:2018年4月24日
*/

package com.xiao.netty.demo.common.basic;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月24日
 * @since JDK 1.8
 */
public abstract class Basic
{
    protected ChannelInitializer<SocketChannel> initializer;
}
