# netty-demo-study
netty学习简单案列demo记录

1.netty入门 -- netty-helloworld

2.netty的粘包 - netty-stick

3.netty支持的各协议，包含messagepack、protobuf以及私有协议  - netty-protocol

4.netty开发httpserver服务 - netty-httpserver

5.netty开发websocket服务 - netty-websocket

6.netty生产级心跳学习，利用IdleStateEvent模拟心跳、断线重连等 - netty-heartbeat

7.netty权威指南中的私有协议栈开发章节，内容包含protocol编解码、心跳、断线重连等。marshalling编解码未走通。 - netty-protocalstack

8.netty+zk实现简单rpc框架 netty-demo-rpc目录

简单RPC实现原理整理
zk：注册中心(netty服务ip/port信息)
 --ServiceDiscovery服务发现
 --ServiceRegistry服务注册
netty：底层通信
 --基于probuffer编解码RpcDecoder、RpcEncoder
RPC请求和响应信息：
 --RpcRequest：请求ID，请求接口类、请求方法名称，请求方法参数类型，请求方法参数
 --RpcResponse：请求ID，响应错误信息，响应消息体
协议接口：HelloService

1.服务端
 1):服务定义注解RpcService
 2):定义服务RpcServer，结合spring，实现ApplicationContextAware, InitializingBean接口，从而可以在方法中通过自定义注解获得用户的业务接口和实现
 3):启动netty服务，并将netty绑定的服务端ip端口 发布到zk上
 4):定义服务HelloService以及实现，注解为RpcService
 5):服务请求到达时由netty的业务hanlder(RpcHandler)处理，从Request中获取类、方法、参数，反射执行方法获取结果并返回netty客户端

2.客户端
 1):注册一个代理RpcProxy服务
 2):代理服务中连接zk，进行服务发现，获取服务端的ip/port信息
 3):通过接口创建一个代理Proxy.newProxyInstance
 4):执行方法调用，拼接RpcRequest
 5):执行netty客户端请求到服务端
 6):等待服务端反射处理将结果返回


参考资料：
 1.《netty权威指南(第二版)》
 2.https://blog.csdn.net/coffeeandice/article/details/79009951  
 对应github:https://github.com/fengjiachun/Jupiter
 3.rpc资料参考
 https://blog.csdn.net/tianjun2012/article/category/6637081
 
 以上如有侵权或疑问请及时联系
 qq:84226733
