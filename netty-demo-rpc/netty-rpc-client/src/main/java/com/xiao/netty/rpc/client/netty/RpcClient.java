/*
 * Winner 
 * 文件名  :RpcClient.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.client.netty;

import com.xiao.netty.rpc.common.coder.RpcDecoder;
import com.xiao.netty.rpc.common.coder.RpcEncoder;
import com.xiao.netty.rpc.common.rpc.RpcRequest;
import com.xiao.netty.rpc.common.rpc.RpcResponse;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 * 框架的RPC 客户端（用于发送 RPC 请求）
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse>
{

    private String host;
    private int port;

    private RpcResponse response;

    private final Object obj = new Object();

    public RpcClient(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception
    {
        this.response = msg;

        synchronized (obj)
        {
            obj.notifyAll();
        }

    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        ctx.close();
    }

    /**
     * 链接服务端，发送消息
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public RpcResponse send(RpcRequest request) throws Exception
    {
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel(SocketChannel channel) throws Exception
                {
                    // 向pipeline中添加编码、解码、业务处理的handler
                    channel.pipeline().addLast(new RpcEncoder(RpcRequest.class)) // OUT - 1
                            .addLast(new RpcDecoder(RpcResponse.class)) // IN - 1
                            .addLast(RpcClient.this); // IN - 2
                }
            }).option(ChannelOption.SO_KEEPALIVE, true);
            // 链接服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();
            // 将request对象写入outbundle处理后发出（即RpcEncoder编码器）
            future.channel().writeAndFlush(request).sync();

            // 用线程等待的方式决定是否关闭连接
            // 其意义是：先在此阻塞，等待获取到服务端的返回后，被唤醒，从而关闭网络连接
            synchronized (obj)
            {
                obj.wait();
            }
            if (response != null)
            {
                future.channel().closeFuture().sync();
            }
            return response;
        }
        finally
        {
            group.shutdownGracefully();
        }
    }

}
