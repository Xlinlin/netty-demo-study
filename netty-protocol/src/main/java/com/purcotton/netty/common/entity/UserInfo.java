/*
 * Winner 
 * 文件名  :UserInfo.java
 * 创建人  :llxiao
 * 创建时间:2018年4月24日
*/

package com.purcotton.netty.common.entity;

import org.msgpack.annotation.Message;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 * 1.MessagePack一定要加 @Message注解，否则无法发送消息<br/>
 * 2.必须要实现 toString方法，否则会出现部分消息丢失 <br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月24日
 * @since JDK 1.8
 */
@Message
public class UserInfo
{
    private String name;
    private int age;

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @author llxiao
     */
    public UserInfo()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @author llxiao
     * @param name
     * @param age
     */
    public UserInfo(String name, int age)
    {
        super();
        this.name = name;
        this.age = age;
    }

    /**
     * 返回name属性
     * 
     * @return name属性
     */
    public String getName()
    {
        return name;
    }

    /**
     * 设置name属性
     * 
     * @param name
     *            name属性
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * 返回age属性
     * 
     * @return age属性
     */
    public int getAge()
    {
        return age;
    }

    /**
     * 设置age属性
     * 
     * @param age
     *            age属性
     */
    public void setAge(int age)
    {
        this.age = age;
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
        return "UserInfo [name=" + name + ", age=" + age + "]";
    }

}
