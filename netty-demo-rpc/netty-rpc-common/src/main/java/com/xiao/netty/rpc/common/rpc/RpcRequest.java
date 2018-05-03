/*
 * Winner 
 * 文件名  :RpcRequest.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.common.rpc;

import java.util.Arrays;

/**
 * [简要描述]:rpc请求<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public class RpcRequest
{
    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数
     */
    private Object[] parameters;

    /**
     * 返回requestId属性
     * 
     * @return requestId属性
     */
    public String getRequestId()
    {
        return requestId;
    }

    /**
     * 设置requestId属性
     * 
     * @param requestId
     *            requestId属性
     */
    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    /**
     * 返回className属性
     * 
     * @return className属性
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * 设置className属性
     * 
     * @param className
     *            className属性
     */
    public void setClassName(String className)
    {
        this.className = className;
    }

    /**
     * 返回methodName属性
     * 
     * @return methodName属性
     */
    public String getMethodName()
    {
        return methodName;
    }

    /**
     * 设置methodName属性
     * 
     * @param methodName
     *            methodName属性
     */
    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    /**
     * 返回parameterTypes属性
     * 
     * @return parameterTypes属性
     */
    public Class<?>[] getParameterTypes()
    {
        return parameterTypes;
    }

    /**
     * 设置parameterTypes属性
     * 
     * @param parameterTypes
     *            parameterTypes属性
     */
    public void setParameterTypes(Class<?>[] parameterTypes)
    {
        this.parameterTypes = parameterTypes;
    }

    /**
     * 返回parameters属性
     * 
     * @return parameters属性
     */
    public Object[] getParameters()
    {
        return parameters;
    }

    /**
     * 设置parameters属性
     * 
     * @param parameters
     *            parameters属性
     */
    public void setParameters(Object[] parameters)
    {
        this.parameters = parameters;
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "RpcRequest [requestId=" + requestId + ", className=" + className + ", methodName=" + methodName
                + ", parameterTypes=" + Arrays.toString(parameterTypes) + ", parameters=" + Arrays.toString(parameters)
                + "]";
    }

}
