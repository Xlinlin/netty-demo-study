/*
 * Winner 
 * 文件名  :RpcHandler.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.server.netty;

import java.lang.reflect.Method;
import java.util.Map;

import com.xiao.netty.rpc.common.rpc.RpcRequest;
import com.xiao.netty.rpc.common.rpc.RpcResponse;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * [简要描述]:处理具体的业务调用<br/>
 * [详细描述]:<br/>
 * 通过构造时传入的“业务接口及实现”handlerMap，来调用客户端所请求的业务方法
 * 并将业务方法返回值封装成response对象写入下一个handler（即编码handler——RpcEncoder）
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest>
{
    // 接口和实现类的实例对象(由spring所构造)
    private final Map<String, Object> handlerMap;

    public RpcHandler(Map<String, Object> handlerMap)
    {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception
    {
        RpcResponse response = new RpcResponse();
        response.setRequestId(msg.getRequestId());
        try
        {
            // 根据request来处理具体的业务调用
            Object result = handle(msg);
            response.setResult(result);
        }
        catch (Throwable t)
        {
            response.setError(t);
        }
        // 写入 outbundle（即RpcEncoder）进行下一步处理（即编码）后发送到channel中给客户端
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }

    /**
     * 根据request来处理具体的业务调用
     * 调用是通过反射的方式来完成
     *
     * @param request
     * @return
     * @throws Throwable
     */
    private Object handle(RpcRequest request) throws Throwable
    {
        String className = request.getClassName();

        // 拿到实现类对象
        Object serviceBean = handlerMap.get(className);

        // 拿到要调用的方法名、参数类型、参数值
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        // 拿到接口类
        Class<?> forName = Class.forName(className);

        // 调用实现类对象的指定方法并返回结果
        Method method = forName.getMethod(methodName, parameterTypes);
        return method.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        // 异常关闭
        ctx.close();
    }
}
