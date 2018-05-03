/*
 * Winner 
 * 文件名  :ConnectionWatchdog.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.heartbeat.prod.client.handler;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

/**
 * [简要描述]:自动重连处理类 <br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
@Sharable
public abstract class ConnectionWatchdog extends ChannelInboundHandlerAdapter implements TimerTask, ChannelHandlerHolder
{
    private final Bootstrap bootstrap;
    private final Timer timer;
    private final int port;

    private final String host;

    private volatile boolean reconnect = true;

    // 重试次数
    private int attempts;

    public ConnectionWatchdog(Bootstrap bootstrap, Timer timer, int port, String host, boolean reconnect)
    {
        this.bootstrap = bootstrap;
        this.timer = timer;
        this.port = port;
        this.host = host;
        this.reconnect = reconnect;
    }

    /**
     * channel链路每次active的时候，将其连接的次数重新☞ 0
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {

        System.out.println("当前链路已经激活了，重连尝试次数重新置为0");
        attempts = 0;
        ctx.fireChannelActive();
    }

    /**
     * [简要描述]:处于非活动状态，这意味着它已关闭<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param ctx
     * @throws Exception
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("链接关闭");
        if (reconnect)
        {
            System.out.println("链接关闭，将进行重连");
            if (attempts < 12)
            {
                attempts++;
                // 重连的间隔时间会越来越长
                int timeout = 2 << attempts;
                timer.newTimeout(this, timeout, TimeUnit.MILLISECONDS);
            }
        }
        ctx.fireChannelInactive();
    }

    /**
     * [简要描述]:netty定时任务<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param timeout
     * @throws Exception
     * @see io.netty.util.TimerTask#run(io.netty.util.Timeout)
     */
    @Override
    public void run(Timeout timeout) throws Exception
    {

        ChannelFuture future;
        // bootstrap已经初始化好了，只需要将handler填入就可以了
        synchronized (bootstrap)
        {
            bootstrap.handler(new ChannelInitializer<Channel>()
            {
                @Override
                protected void initChannel(Channel ch) throws Exception
                {
                    ch.pipeline().addLast(handlers());
                }
            });
            future = bootstrap.connect(host, port);
        }
        // future对象
        future.addListener(new ChannelFutureListener()
        {
            public void operationComplete(ChannelFuture f) throws Exception
            {
                boolean succeed = f.isSuccess();

                // 如果重连失败，
                if (!succeed)
                {
                    System.out.println("重连失败");
                    // 则调用ChannelInactive方法，再次出发重连事件，一直尝试12次，如果失败则不再重连
                    f.channel().pipeline().fireChannelInactive();
                }
                else
                {
                    System.out.println("重连成功");
                }
            }
        });

    }
}
