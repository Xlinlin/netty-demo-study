/*
 * Winner 
 * 文件名  :TestHttpServer.java
 * 创建人  :llxiao
 * 创建时间:2018年4月25日
*/

package com.xiao.netty.demo.http.handler;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;

import org.junit.Test;

import com.xiao.netty.demo.common.server.Server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月25日
 * @since JDK 1.8
 */
public class TestHttpServer
{
    @Test
    public void testHttpServer()
    {
        startHttpServer(8000, null);
    }

    @Test
    public void testHttpsServer()
    {
        SslContext sslContext = initSsl();
        startHttpServer(443, sslContext);
    }

    private void startHttpServer(int port, SslContext sslContext)
    {
        ChannelHandler serviceHandler = new HttpServerHandler();
        ChannelInitializer<SocketChannel> initializer = new HttpServerChannelInitializer(serviceHandler, sslContext);
        Server server = new Server(port, initializer);
        server.start();
    }

    /**
     * [简要描述]:ssl初始化<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     */
    private SslContext initSsl()
    {
        SslContext sslContext = null;
        String keyStoreFilePath = "/root/.ssl/test.pkcs12";
        String keyStorePassword = "Password@123";

        try
        {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(keyStoreFilePath), keyStorePassword.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

            sslContext = SslContextBuilder.forServer(keyManagerFactory).build();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return sslContext;
    }
}
