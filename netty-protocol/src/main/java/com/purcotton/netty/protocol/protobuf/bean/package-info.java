/*
 * Winner 
 * 文件名  :package-info.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

/**
 * [简要描述]:protobuf是由Google开发的一套对数据结构进行序列化的方法，可用做通信协议，数据存储格式，等等。其特点是不限语言、不限平台、扩展性强<br/>
 * [详细描述]:Netty也提供了对Protobuf的天然支持<br/>
 * 1.编写protobuf格式的文件 .proto，语法格式参考：https://blog.csdn.net/u011518120/article/details/54604615<br/>
 * 2.下载工具转成java代码，下载连接：https://github.com/google/protobuf/releases<br/>
 * 2.1 对应版本 windows、linux
 * 2.2 windows执行命令参考： protoc.exe proto文件 --java_out=java文件输出路径
 * 3.生成的文件复制到Java工程
 * 4.引用googole的protobuf包，版本必须保持一致，即转代码工具的版本和jar的版本。如3.5.1
 * 5.案例参考 RichMan.proto文件
 * 
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
package com.purcotton.netty.protocol.protobuf.bean;