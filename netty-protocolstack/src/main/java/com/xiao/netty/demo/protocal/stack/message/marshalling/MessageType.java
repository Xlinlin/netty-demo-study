/*
 * Winner 
 * 文件名  :MessageType.java
 * 创建人  :llxiao
 * 创建时间:2018年4月28日
*/

package com.xiao.netty.demo.protocal.stack.message.marshalling;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月28日
 * @since JDK 1.8
 */
public enum MessageType
{
    /**
     * 请求
     */
    REQ((byte) 0),
    /**
     * 响应
     */
    RESP((byte) 1),

    /**
     * 业务请求，既是请求也是响应
     */
    SERVICE((byte) 2),

    /**
     * 握手请求
     */
    SHAKE_HANDS_REQ((byte) 3),

    /**
     * 握手应答
     */
    SHAKE_HANDS_RESP((byte) 4),

    /**
     * 心跳ping
     */
    PING((byte) 5),

    /**
     * 心跳pong
     */
    PONG((byte) 6),

    /**
     * 握手成功
     */
    SHAKE_HANDS_SUCCESS((byte) 0),

    /**
     * 握手失败
     */
    SHAKE_HANDS_FAILD((byte) -1);

    private byte type;

    MessageType(byte type)
    {
        this.type = type;
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
}
