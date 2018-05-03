/*
 * Winner 
 * 文件名  :package-info.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 * 拆包器：服务端在pipeline中 第一个指定 ch.pipeline().addLast(Decoder)<br>
 * 1. LineBasedFrameDecoder 行标记拆包器，客户端使用System.getProperty("line.separator")标记一个完整包，解决单次长包粘包问题<br>
 * 2. FixedLengthFrameDecoder 定长拆包器，解决单次多包粘包问题<br>
 * 3. DelimiterBasedFrameDecoder 自定义固定字符拆包器<br>
 * 4. 上述三个基本解码器，全部继承于ByteToMessageDecoder<br>
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
package com.purcotton.netty.demo.stick;