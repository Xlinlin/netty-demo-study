/*
 * Winner 
 * 文件名  :RpcService.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
*/

package com.xiao.netty.rpc.server.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;


/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
@Documented
//VM将在运行期也保留注释，因此可以通过过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)
//注解用在接口上
@Target({ElementType.TYPE})
@Component
public @interface RpcService
{
    Class<?> value();
}
