/*
 * Winner 
 * 文件名  :MesageHeader.java
 * 创建人  :llxiao
 * 创建时间:2018年4月26日
*/

package com.xiao.netty.demo.protocal.stack.message.marshalling;

import java.util.Map;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月26日
 * @since JDK 1.8
 */
public class MessageHeader
{

    /**
     * 消息校验码：
     * 1.固定值0xABEF，netty协议消息，占2个字节；
     * 2.01主版本号，占1字节；
     * 3.01次版本号，占1字节。
     * magicCode = 0xABEF + 主版本号 + 次版本号
     * (int 32长度)
     */
    private int magicCode = 0xABEF0101;

    /**
     * 消息长度
     * 包括：header和body
     * (int 32长度)
     */
    private int length;

    /**
     * 节点全局唯一，会话ID
     * (long 64长度)
     */
    private long sessionId;

    /**
     * 消息类型：
     * 0.业务请求
     * 1.业务响应
     * 2.业务ONE WAY消息（既是请求又是响应消息）
     * 3.握手请求
     * 4.握手应答
     * 5.心跳请求
     * 6.心跳应答
     * (byte 8长度)
     */
    private byte type;

    /**
     * 消息优先级：0~255
     * (byte 8长度)
     */
    private byte priority;

    /**
     * 扩展字段
     */
    private Map<String, Object> attachment;

    /**
     * 返回magicCode属性
     * 
     * @return magicCode属性
     */
    public int getMagicCode()
    {
        return magicCode;
    }

    /**
     * 设置magicCode属性
     * 
     * @param magicCode
     *            magicCode属性
     */
    public void setMagicCode(int magicCode)
    {
        this.magicCode = magicCode;
    }

    /**
     * 返回length属性
     * 
     * @return length属性
     */
    public int getLength()
    {
        return length;
    }

    /**
     * 设置length属性
     * 
     * @param length
     *            length属性
     */
    public void setLength(int length)
    {
        this.length = length;
    }

    /**
     * 返回sessionId属性
     * 
     * @return sessionId属性
     */
    public long getSessionId()
    {
        return sessionId;
    }

    /**
     * 设置sessionId属性
     * 
     * @param sessionId
     *            sessionId属性
     */
    public void setSessionId(long sessionId)
    {
        this.sessionId = sessionId;
    }

    /**
     * 返回type属性
     * 
     * @return type属性
     */
    public byte getType()
    {
        return type;
    }

    /**
     * 设置type属性
     * 
     * @param type
     *            type属性
     */
    public void setType(byte type)
    {
        this.type = type;
    }

    /**
     * 返回priority属性
     * 
     * @return priority属性
     */
    public byte getPriority()
    {
        return priority;
    }

    /**
     * 设置priority属性
     * 
     * @param priority
     *            priority属性
     */
    public void setPriority(byte priority)
    {
        this.priority = priority;
    }

    /**
     * 返回attachment属性
     * 
     * @return attachment属性
     */
    public Map<String, Object> getAttachment()
    {
        return attachment;
    }

    /**
     * 设置attachment属性
     * 
     * @param attachment
     *            attachment属性
     */
    public void setAttachment(Map<String, Object> attachment)
    {
        this.attachment = attachment;
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
        return "MesageHeader [magicCode=" + magicCode + ", length=" + length + ", sessionId=" + sessionId + ", type="
                + type + ", priority=" + priority + ", attachment=" + attachment + "]";
    }

}
