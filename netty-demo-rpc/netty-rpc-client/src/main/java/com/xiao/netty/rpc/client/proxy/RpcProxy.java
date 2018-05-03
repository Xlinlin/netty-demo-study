/*
 * Winner 
 * 文件名  :RpcProxy.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import com.xiao.netty.rpc.client.netty.RpcClient;
import com.xiao.netty.rpc.common.rpc.RpcRequest;
import com.xiao.netty.rpc.common.rpc.RpcResponse;
import com.xiao.netty.rpc.common.zk.ServiceDiscovery;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public class RpcProxy
{
    private String serverAddress;
    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(String serverAddress)
    {
        this.serverAddress = serverAddress;
    }

    public RpcProxy(ServiceDiscovery serviceDiscovery)
    {
        this.serviceDiscovery = serviceDiscovery;
    }

    /**
     * 创建代理
     * 
     * @param interfaceClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass)
    {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]
        {
                interfaceClass
        }, new InvocationHandler()
        {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
            {
                // 创建RpcRequest，封装被代理类的属性
                RpcRequest request = new RpcRequest();
                request.setRequestId(UUID.randomUUID().toString());
                // 拿到声明这个方法的业务接口名称
                request.setClassName(method.getDeclaringClass().getName());
                request.setMethodName(method.getName());
                request.setParameterTypes(method.getParameterTypes());
                request.setParameters(args);
                // 查找服务
                if (serviceDiscovery != null)
                {
                    serverAddress = serviceDiscovery.discovery();
                }
                // 随机获取服务的地址
                String[] array = serverAddress.split(":");
                String host = array[0];
                int port = Integer.parseInt(array[1]);
                // 创建Netty实现的RpcClient，链接服务端
                RpcClient client = new RpcClient(host, port);
                // 通过netty向服务端发送请求
                RpcResponse response = client.send(request);
                // 返回信息
                if (response.isError())
                {
                    throw response.getError();
                }
                else
                {
                    return response.getResult();
                }
            }
        });
    }
}
