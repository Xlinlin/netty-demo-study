/*
 * Winner 
 * 文件名  :Constant.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.common;

/**
 * [简要描述]:公共常量池<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public interface Constant
{
    /**
     * zk超时事件
     */
    int ZK_SESSION_TIMEOUT = 5000;

    /**
     * ZK注册节点
     */
    String REGISTRY_BASIC_PAHT = "/rpc";

    /**
     * ZK数据节点
     */
    String REGISTRY_DATA_PAHT = "/rpc/data";
}
