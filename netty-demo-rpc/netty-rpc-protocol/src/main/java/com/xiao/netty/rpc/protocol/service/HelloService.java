/*
 * Winner 
 * 文件名  :HelloService.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.protocol.service;

import com.xiao.netty.rpc.protocol.dto.Person;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public interface HelloService
{
    String hello(String name);

    String hello(Person person);
}
