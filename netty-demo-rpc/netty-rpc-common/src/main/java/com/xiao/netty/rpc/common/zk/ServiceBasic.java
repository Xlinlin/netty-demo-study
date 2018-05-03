/*
 * Winner 
 * 文件名  :ServiceBasic.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.common.zk;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.xiao.netty.rpc.common.Constant;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public abstract class ServiceBasic
{
    private CountDownLatch latch = new CountDownLatch(1);

    /**
     * 服务注册地址
     */
    protected String registryAddress;

    /**
     * [简要描述]:连接ZK<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @return
     */
    protected ZooKeeper connectZk()
    {
        ZooKeeper zk = null;
        try
        {
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher()
            {

                @Override
                public void process(WatchedEvent event)
                {
                    if (event.getState() == Event.KeeperState.SyncConnected)
                    {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        }
        catch (IOException | InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return zk;
    }
}
