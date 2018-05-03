/*
 * Winner 
 * 文件名  :StackReconnectionHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年4月28日
*/

package com.xiao.netty.demo.protocal.stack.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xiao.netty.demo.common.client.Client;
import com.xiao.netty.demo.common.client.ReconnectionHandler;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
public class ClientReconnectionHandler implements ReconnectionHandler, Runnable
{

    // 默认5S，随着重连的次数依次递增
    private volatile long time = 5;

    private ExecutorService excecutor = Executors.newScheduledThreadPool(1);

    private Client client;

    public ClientReconnectionHandler(Client client)
    {
        this.client = client;
    }

    @Override
    public void reconnection()
    {
        excecutor.execute(this);
    }

    @Override
    public void run()
    {
        // 重连等待时间一次增大
        System.out.println("Reconnection ............");
        try
        {
            Thread.sleep(time * 1000);
            // 进行重连
            client.start();

            if (client.connected())
            {
                // 重连成功，清除
                time = 5;
                System.out.println("Reconnection Success!");
                System.out.println("Reset connect time!");
            }
            else
            {
                // 重连N次，放弃重连
                if (time / 5 == 5)
                {
                    System.out.println("Reconnection timeout! Please check server!");
                    excecutor.shutdownNow();
                    client.shutdownGracefully();
                }
                else
                {
                    time += 5;
                    System.out.println("Next wait time:" + time);
                }
            }
        }
        catch (InterruptedException e)
        {
            System.out.println("Connection exception:" + e.toString());
        }

    }

}
