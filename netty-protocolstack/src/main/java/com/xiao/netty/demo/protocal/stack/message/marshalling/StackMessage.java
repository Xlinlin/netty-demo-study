/*
 * Winner 
 * 文件名  :StackMessage.java
 * 创建人  :llxiao
 * 创建时间:2018年4月26日
*/

package com.xiao.netty.demo.protocal.stack.message.marshalling;

import org.msgpack.annotation.Message;

/**
 * [简要描述]:协议栈消息定义<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月26日
 * @since JDK 1.8
 */
@Message
public class StackMessage
{

    /**
     * 消息头
     */
    private MessageHeader header;

    /**
     * 消息体
     */
    private Object body;

    /**
     * 返回header属性
     * 
     * @return header属性
     */
    public MessageHeader getHeader()
    {
        return header;
    }

    /**
     * 设置header属性
     * 
     * @param header
     *            header属性
     */
    public void setHeader(MessageHeader header)
    {
        this.header = header;
    }

    /**
     * 返回body属性
     * 
     * @return body属性
     */
    public Object getBody()
    {
        return body;
    }

    /**
     * 设置body属性
     * 
     * @param body
     *            body属性
     */
    public void setBody(Object body)
    {
        this.body = body;
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
        return "StackMessage [header=" + header + ", body=" + body + "]";
    }

}
