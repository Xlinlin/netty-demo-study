/*
 * Winner 
 * 文件名  :ServiceRegistry.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.common.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;

import com.xiao.netty.rpc.common.Constant;

import org.apache.zookeeper.ZooKeeper;

/**
 * [简要描述]:服务注册<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public class ServiceRegistry extends ServiceBasic
{
    public ServiceRegistry(String registryAddress)
    {
        this.registryAddress = registryAddress;
    }

    /**
     * 创建zookeeper链接
     * 
     * @param data
     */
    public void register(String data)
    {
        if (data != null)
        {
            ZooKeeper zk = this.connectZk();
            if (zk != null)
            {
                createNode(zk, data);
            }
        }
    }

    /**
     * [简要描述]:注册一个节点<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param zk
     * @param data
     */
    private void createNode(ZooKeeper zk, String data)
    {
        try
        {
            byte[] bytes = data.getBytes();
            if (zk.exists(Constant.REGISTRY_BASIC_PAHT, null) == null)
            {
                zk.create(Constant.REGISTRY_BASIC_PAHT, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            String path = zk.create(Constant.REGISTRY_DATA_PAHT, bytes, Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("create zookeeper node (" + path + " => " + data + ")");
        }
        catch (Exception e)
        {
            // TODO
            e.printStackTrace();
        }
    }
}
