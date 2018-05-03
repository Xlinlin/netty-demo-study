/*
 * Winner 
 * 文件名  :RpcResponse.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.common.rpc;

/**
 * [简要描述]:请求响应数据<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public class RpcResponse
{
    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 请求错误信息
     */
    private Throwable error;

    /**
     * 响应结果
     */
    private Object result;

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
     * 返回error属性
     * 
     * @return error属性
     */
    public Throwable getError()
    {
        return error;
    }

    /**
     * 设置error属性
     * 
     * @param error
     *            error属性
     */
    public void setError(Throwable error)
    {
        this.error = error;
    }

    /**
     * 返回result属性
     * 
     * @return result属性
     */
    public Object getResult()
    {
        return result;
    }

    /**
     * 设置result属性
     * 
     * @param result
     *            result属性
     */
    public void setResult(Object result)
    {
        this.result = result;
    }

    public boolean isError()
    {
        return this.error != null;
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
        return "RpcResponse [requestId=" + requestId + ", error=" + error + ", result=" + result + "]";
    }

}
