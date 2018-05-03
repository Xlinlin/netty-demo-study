/*
 * Winner 
 * 文件名  :package-info.java
 * 创建人  :llxiao
 * 创建时间:2018年4月3日
*/

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 * +------------------+----------------+---------------+  <br>
 * |discardable bytes | readable bytes | writable bytes|  <br>
 * |------------------|--(CONTENT)-----|---------------|  <br>
 * |                  |                |               |  <br>
 * 0      <=    readerIndex    <=    writerIndex  <=   capacity<br>
 * 1.ByteBuf都是维护一个byte数组,维护了两个指针，一个是读指针，一个是写指针<br>
 * 2.可读的区域是下标区间是[readerIndex，writeIndex)，可写区间的是[writerIndex,capacity-1]<br>
 * 3.内存回收角度：内存池ByteBuf->PooledByteBuf 和普通ByteBuf ->UnpooledByteBuf。内存池效率高，维护复杂 <br>
 * 4.内存分角度：堆内存HeapByteBuf 和直接内存DirectByteBuf。<br>
 * 4.1 堆内存分配和回收快，由于Socket I/O时需要额外的Copy到内核，因此性能相对较低<br>
 * 4.2 堆外分配，相对分配和回收速度要慢，Socket I/O读写要快<br>
 * 还有几个比较重要的API:<br>
 * 1）duplicate方法：复制当前对象，复制后的对象与前对象共享缓冲区，且维护自己的独立索引<br>
 * 2）copy方法：复制一份全新的对象，内容和缓冲区都不是共享的<br>
 * 3）slice方法：获取调用者的子缓冲区，且与原缓冲区共享缓冲区<br>
 * 
 * @author llxiao
 * @version 1.0, 2018年4月3日
 * @since PURCOTTON 1.0
 */
package com.purcotton.netty.demo.helloworld.bytebuf;