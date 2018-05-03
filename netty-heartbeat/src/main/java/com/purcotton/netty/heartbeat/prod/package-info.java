/*
 * Winner 
 * 文件名  :package-info.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

/**
 * [简要描述]:Netty经典心跳与重连机制（生产级）<br/>
 * [详细描述]:<br/>
 * 整个心跳测试与重连的思路大体相同，基本是如下6个步骤:<br>
 * 1）客户端连接服务端<b>
 * 2）在客户端的的ChannelPipeline中加入一个比较特殊的IdleStateHandler，设置一下客户端的写空闲时间，例如5s<br>
 * 3）当客户端的所有ChannelHandler中4s内没有write事件，则会触发userEventTriggered方法（查看gitHub）<br>
 * 4）我们在客户端的userEventTriggered中对应的触发事件下发送一个心跳包给服务端，检测服务端是否还存活，防止服务端已经宕机，客户端还不知道<br>
 * 5）同样，服务端要对心跳包做出响应，其实给客户端最好的回复就是“不回复”，这样可以服务端的压力<br>
 * 假如有10w个空闲Idle的连接，那么服务端光发送心跳回复，则也是费事的事情，那么怎么才能告诉客户端它还活着呢？<br>
 * 其实很简单，因为5s服务端都会收到来自客户端的心跳信息，那么如果10秒内收不到，服务端可以认为客户端挂了，可以close链路<br>
 * 6）假如服务端因为什么因素导致宕机的话，就会关闭所有的链路链接，所以作为客户端要做的事情就是短线重连<br>
 * 
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
package com.purcotton.netty.heartbeat.prod;