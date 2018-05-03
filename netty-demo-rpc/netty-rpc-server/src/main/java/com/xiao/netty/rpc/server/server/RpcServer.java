/*
 * Winner 
 * 文件名  :RpcServer.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.server.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.xiao.netty.rpc.common.coder.RpcDecoder;
import com.xiao.netty.rpc.common.coder.RpcEncoder;
import com.xiao.netty.rpc.common.rpc.RpcRequest;
import com.xiao.netty.rpc.common.rpc.RpcResponse;
import com.xiao.netty.rpc.common.zk.ServiceRegistry;
import com.xiao.netty.rpc.server.annotation.RpcService;
import com.xiao.netty.rpc.server.netty.RpcHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 * 框架的RPC 服务器（用于将用户系统的业务类发布为 RPC 服务）<br/>
 * 使用时可由用户通过spring-bean的方式注入到用户的业务系统中<br/>
 * 由于本类实现了ApplicationContextAware InitializingBean<br/>
 * spring构造本对象时会调用setApplicationContext()方法，从而可以在方法中通过自定义注解获得用户的业务接口和实现<br/>
 * 还会调用afterPropertiesSet()方法，在方法中启动netty服务器<br/>
 * 
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public class RpcServer implements ApplicationContextAware, InitializingBean
{

    private String serverAddress;

    /**
     * 服务注册
     */
    private ServiceRegistry serviceRegistry;

    // 用于存储业务接口和实现类的实例对象(由spring所构造)
    private Map<String, Object> handlerMap = new HashMap<String, Object>();

    public RpcServer(String serverAddress)
    {
        this.serverAddress = serverAddress;
    }

    // 服务器绑定的地址和端口由spring在构造本类时从配置文件中传入
    public RpcServer(String serverAddress, ServiceRegistry serviceRegistry)
    {
        this.serverAddress = serverAddress;
        // 用于向zookeeper注册名称服务的工具类
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     * 在此启动netty服务，绑定handle流水线：<br/>
     * 1、接收请求数据进行反序列化得到request对象<br/>
     * 2、根据request中的参数，让RpcHandler从handlerMap中找到对应的业务imple，调用指定方法，获取返回结果<br/>
     * 3、将业务调用结果封装到response并序列化后发往客户端<br/>
     * 
     * @author llxiao
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception
                        {
                            // 注册解码 IN-1
                            channel.pipeline().addLast(new RpcDecoder(RpcRequest.class))
                                    // 注册编码 OUT
                                    .addLast(new RpcEncoder(RpcResponse.class))
                                    // 注册RpcHandler IN-2
                                    .addLast(new RpcHandler(handlerMap));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            ChannelFuture future = bootstrap.bind(host, port).sync();

            if (serviceRegistry != null)
            {
                serviceRegistry.register(serverAddress);
            }

            future.channel().closeFuture().sync();
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     * 通过注解，获取标注了rpc服务注解的业务类的----接口及impl对象，将它放到handlerMap中
     * 
     * @author llxiao
     * @param applicationContext
     * @throws BeansException
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException
    {
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
        String interfaceName = "";
        if (MapUtils.isNotEmpty(serviceBeanMap))
        {
            for (Object serviceBean : serviceBeanMap.values())
            {
                // 从业务实现类上的自定义注解中获取到value，从而获取到业务接口的全名
                interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                handlerMap.put(interfaceName, serviceBean);
            }
        }
    }

}
