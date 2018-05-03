/*
 * Winner 
 * 文件名  :HttpServerChannelInitializer.java
 * 创建人  :llxiao
 * 创建时间:2018年4月25日
*/

package com.xiao.netty.demo.http.handler;

import javax.net.ssl.SSLEngine;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * [简要描述]:<br/>
 * [详细描述]:http服务ChannelInitializer<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月25日
 * @since JDK 1.8
 */
public class HttpServerChannelInitializer extends ChannelInitializer<SocketChannel>
{

    /**
     * 业务服务handler
     */
    private ChannelHandler serviceHandler;

    /**
     * SSL证书
     */
    private SslContext sslContext;

    public HttpServerChannelInitializer(ChannelHandler serviceHandler, SslContext sslContext)
    {
        this.serviceHandler = serviceHandler;
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception
    {
        System.out.println("initChannel ch:" + ch);

        if (null != sslContext)
        {
            // 添加ssl handler
            SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
            ch.pipeline().addLast(new SslHandler(sslEngine));
        }

        // http 服务 decoder encoder
        // 解码request
        ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
        // 消息聚合器（重要）
        /*
         * 为什么能有FullHttpRequest这个东西，就是因为有他，HttpObjectAggregator，如果没有他，
         * 就不会有那个消息是FullHttpRequest的那段Channel，同样也不会有FullHttpResponse
         * HttpObjectAggregator(512 * 1024)的参数含义是消息合并的数据大小，如此代表聚合的消息内容长度不超过512kb。
         * 将多个消息转换为单一的FullHttpRequest或FullHttpResponse
         * HTTP解码器在每个HTTP消息中会生成多个消息对象：
         * 1.HttpRequest/HttpResponse
         * 2.HttpContent
         * 3.LastHttpContet
         */
        ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(512 * 1024));
        // 编码response
        ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
        // 支持异步发送大码流，但不占用大内存，防止内存溢出
        ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());

        // 业务处理
        ch.pipeline().addLast("http-service", serviceHandler);
    }

}
