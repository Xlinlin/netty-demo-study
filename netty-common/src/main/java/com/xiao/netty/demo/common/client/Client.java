/*
 * Winner 
 * 文件名  :Client.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.xiao.netty.demo.common.client;

import java.util.concurrent.atomic.AtomicBoolean;

import com.xiao.netty.demo.common.basic.Basic;
import com.xiao.netty.demo.common.basic.NettyConnection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since JDK-1.8
 */
public class Client extends Basic implements NettyConnection
{
    private String host = "127.0.0.1";

    private int port;

    private ReconnectionHandler reconnectHandler;

    private EventLoopGroup group = null;

    private AtomicBoolean success = new AtomicBoolean(false);

    /**
     * [简要描述]:客户端构造器<br/>
     * [详细描述]:<br/>
     *
     * @author llxiao
     * @param host
     *            服务端主机
     * @param port
     *            服务端端口
     * @param initializer
     *            ChannelInitializer<SocketChannel>
     */
    public Client(String host, int port, ChannelInitializer<SocketChannel> initializer)
    {
        this.host = host;
        this.port = port;
        this.initializer = initializer;
    }

    /**
     * [简要描述]:启动服务<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     */
    @Override
    public void start()
    {
        // 客户端需要一个事件群组即可
        group = new NioEventLoopGroup();
        try
        {
            Bootstrap client = new Bootstrap();
            client.group(group);
            // 指定 Channel 的类型. 因为是客户端, 因此使用了 NioSocketChannel.
            client.channel(NioSocketChannel.class);
            // option，提供了一系列的TCP参数
            client.option(ChannelOption.TCP_NODELAY, true);
            client.handler(initializer);

            ChannelFuture future = client.connect(host, port).sync();
            // 绑定本地端口(设置本地IP和端口(客户端))，主要用户服务端重新登录保护，一般情况下不允许系统随便使用随机端口
            // ChannelFuture future = client.connect(new InetSocketAddress(host, port),
            // new InetSocketAddress(NettyConstant.LOCALIP, NettyConstant.LOCAL_PORT)).sync();
            // 连接成功
            success.set(true);
            future.channel().closeFuture().sync();
        }
        catch (Exception e)
        {
            // 异常进行重连
            success.set(false);
            // e.printStackTrace();
            System.out.println("Connection exception : " + e.toString());
        }
        finally
        {

            if (null == reconnectHandler)
            {
                shutdownGracefully();
            }
            else
            {
                // 所有资源释放完成之后，清空资源，再次发起重连操作
                reconnectHandler.reconnection();
            }
        }
    }

    /**
     * [简要描述]:关闭客户端连接<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     */
    public void shutdownGracefully()
    {
        System.out.println("Exit client connection!!");
        group.shutdownGracefully();
    }

    /**
     * [简要描述]:连接成功<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @return
     */
    public boolean connected()
    {
        return this.success.get();
    }

    /**
     * 返回reconnectHandler属性
     * 
     * @return reconnectHandler属性
     */
    public ReconnectionHandler getReconnectHandler()
    {
        return reconnectHandler;
    }

    /**
     * 设置reconnectHandler属性
     * 
     * @param reconnectHandler
     *            reconnectHandler属性
     */
    public void setReconnectHandler(ReconnectionHandler reconnectHandler)
    {
        this.reconnectHandler = reconnectHandler;
    }

}
