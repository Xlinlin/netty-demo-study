/*
 * Winner 
 * 文件名  :MessageConstants.java
 * 创建人  :llxiao
 * 创建时间:2018年4月29日
*/

package com.xiao.netty.demo.protocal.stack.message;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月29日
 * @since JDK 1.8
 */
public interface MessageConstants
{
    /**
     * 内部协议标识头
     */
    int MAC_NUM = 0xABCD;
    
    /**
     * 握手成功
     */
    String SHAKE_HANDS_SUCCESS = "0";
    
    /**
     * 握手失败
     */
    String SHAKE_HANDS_FAILD = "-1";
}
