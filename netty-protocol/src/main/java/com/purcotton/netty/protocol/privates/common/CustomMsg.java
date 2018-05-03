/*
 * Winner 
 * 文件名  :CustomMsg.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

package com.purcotton.netty.protocol.privates.common;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 * 1）type表示发送端的系统类型
 * 2）flag表示发送信息的类型，是业务数据，还是心跳包数据
 * 3）length表示主题body的长度
 * 4）body表示主题信息
 *
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
public class CustomMsg
{
    // 类型 系统编号 0xAB 表示A系统，0xBC 表示B系统
    private byte type;

    // 信息标志 0xAB 表示心跳包 0xBC 表示超时包 0xCD 业务信息包
    private byte flag;

    // 主题信息的长度
    private int length;

    // 主题信息
    private String body;

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @author llxiao
     */
    public CustomMsg()
    {
        super();
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @author llxiao
     * @param type
     * @param flag
     * @param length
     * @param body
     */
    public CustomMsg(byte type, byte flag, int length, String body)
    {
        super();
        this.type = type;
        this.flag = flag;
        this.length = length;
        this.body = body;
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
     * 返回flag属性
     * 
     * @return flag属性
     */
    public byte getFlag()
    {
        return flag;
    }

    /**
     * 设置flag属性
     * 
     * @param flag
     *            flag属性
     */
    public void setFlag(byte flag)
    {
        this.flag = flag;
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
     * 返回body属性
     * 
     * @return body属性
     */
    public String getBody()
    {
        return body;
    }

    /**
     * 设置body属性
     * 
     * @param body
     *            body属性
     */
    public void setBody(String body)
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
        return "CustomMsg [type=" + type + ", flag=" + flag + ", length=" + length + ", body=" + body + "]";
    }

}
