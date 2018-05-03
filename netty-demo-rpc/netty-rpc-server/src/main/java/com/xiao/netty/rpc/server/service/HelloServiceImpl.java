/*
 * Winner 
 * 文件名  :HelloServiceImpl.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.server.service;

import com.xiao.netty.rpc.protocol.dto.Person;
import com.xiao.netty.rpc.protocol.service.HelloService;
import com.xiao.netty.rpc.server.annotation.RpcService;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService
{

    @Override
    public String hello(String name)
    {
        System.out.println("已经调用服务端接口实现，业务处理结果为：");
        System.out.println("Hello! " + name);
        return "RPC入门! " + name;
    }

    @Override
    public String hello(Person person)
    {
        System.out.println("已经调用服务端接口实现，业务处理为：");
        System.out.println("Hello! " + person.getFirstName() + " " + person.getLastName());
        return "Rpc test! " + person.getFirstName() + " " + person.getLastName();
    }

}
