/*
 * Winner 
 * 文件名  :MyReadTimeoutHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.demo.protocal.stack.handler.common;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
@Sharable
public class MyReadTimeoutHandler extends ReadTimeoutHandler
{

    public MyReadTimeoutHandler(int timeoutSeconds)
    {
        super(timeoutSeconds);
    }

}
