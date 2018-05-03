/*
 * Winner 
 * 文件名  :ServiceDiscovery.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.common.zk;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.xiao.netty.rpc.common.Constant;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.ThreadLocalRandom;

/**
 * [简要描述]:<br/>
 * [详细描述]:服务发现<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public class ServiceDiscovery extends ServiceBasic
{

    /**
     * 节点数据
     */
    private volatile List<String> dataList = new ArrayList<String>();

    public ServiceDiscovery(String registryAddress)
    {
        this.registryAddress = registryAddress;

        // 初始zk连接
        ZooKeeper zk = connectZk();

        if (null != zk)
        {
            // 监听ZK节点信息
            watchZkNode(zk);
        }
    }

    /**
     * [简要描述]:服务发现<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @return
     */
    public String discovery()
    {
        String data = null;
        int size = dataList.size();
        // 存在新节点，使用即可
        if (size > 0)
        {
            if (size == 1)
            {
                data = dataList.get(0);
            }
            else
            {
                // 随机取一个
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
            }
        }
        return data;
    }

    /**
     * [简要描述]:监听节点信息<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param zk
     */
    private void watchZkNode(ZooKeeper zk)
    {
        try
        {
            List<String> nodeInfos = zk.getChildren(Constant.REGISTRY_BASIC_PAHT, new Watcher()
            {

                @Override
                public void process(WatchedEvent event)
                {
                    // 节点发生变化，更新节点数据
                    if (event.getType() == Event.EventType.NodeChildrenChanged)
                    {
                        watchZkNode(zk);
                    }
                }
            });
            // 更新zk上的数据，保存到当前节点中
            List<String> dataList = new ArrayList<String>();
            for (String nodeInfo : nodeInfos)
            {
                byte[] data = zk.getData(Constant.REGISTRY_BASIC_PAHT + "/" + nodeInfo, false, null);
                dataList.add(new String(data, CharsetUtil.UTF_8));
            }
            this.dataList = dataList;
            System.out.println("Node info:" + dataList);
        }
        catch (KeeperException | InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
