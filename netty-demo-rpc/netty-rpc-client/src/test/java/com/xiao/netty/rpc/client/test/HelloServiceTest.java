/*
 * Winner 
 * 文件名  :HelloServiceTest.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.client.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xiao.netty.rpc.client.proxy.RpcProxy;
import com.xiao.netty.rpc.protocol.dto.Person;
import com.xiao.netty.rpc.protocol.service.HelloService;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class HelloServiceTest
{
    @Autowired
    private RpcProxy rpcProxy;

    @Test
    public void helloTest1()
    {
        // 调用代理的create方法，代理HelloService接口
        HelloService helloService = rpcProxy.create(HelloService.class);

        // 调用代理的方法，执行invoke
        String result = helloService.hello("World");
        System.out.println("服务端返回结果：");
        System.out.println(result);
    }

    @Test
    public void helloTest2()
    {
        HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.hello(new Person("Tian", "Jun"));
        System.out.println("服务端返回结果：");
        System.out.println(result);
    }
}
