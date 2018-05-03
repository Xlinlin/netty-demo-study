/*
 * Winner 
 * 文件名  :WebsocketChannelInitializer.java
 * 创建人  :llxiao
 * 创建时间:2018年4月25日
*/

package com.xiao.netty.demo.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * [简要描述]:Websocket ChannelInitializer<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月25日
 * @since JDK 1.8
 */
public class WebsocketChannelInitializer extends ChannelInitializer<SocketChannel>
{

    private ChannelHandler websocketChannel;

    public WebsocketChannelInitializer(ChannelHandler websocketChannel)
    {
        this.websocketChannel = websocketChannel;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception
    {

        // HttpServerCodec 包含HttpServerRequestDecoder和HttpServerResponseEncoder
        ch.pipeline().addLast("http-codec", new HttpServerCodec());

        ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(512 * 2014));
        // 支持浏览器和服务端进行Websocket通信
        ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());

        /*
         * ws://server:port/context_path
         * ws://localhost:9999/ws
         * 参数指的是contex_path
         */
        // 指定的是ws，服务客户端访问服务器的时候指定的url是：ws://localhost:8899/ws
        // 负责websocket握手以及处理控制框架（Close，Ping（心跳检检测request），Pong（心跳检测响应））
        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));

        // 业务处理
        ch.pipeline().addLast("websocket-handler", websocketChannel);
    }

}
