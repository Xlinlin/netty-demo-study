/*
 * Winner 
 * 文件名  :RpcBootstrap.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 * 用户系统服务端的启动入口,其意义是启动springcontext，从而构造框架中的RpcServer<br>
 * 亦即：将用户系统中所有标注了RpcService注解的业务发布到RpcServer中<br>
 * 参考连接：https://blog.csdn.net/tianjun2012/article/details/53893677<br>
 * 
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
@SuppressWarnings("resource")
public class RpcBootstrap
{
    public static void main(String[] args)
    {
        new ClassPathXmlApplicationContext("spring.xml");
    }
}
