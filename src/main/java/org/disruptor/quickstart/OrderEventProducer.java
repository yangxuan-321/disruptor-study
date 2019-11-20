package org.disruptor.quickstart;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @author Kevin
 * @Title: OrderEventProducer
 * @ProjectName disruptor-study
 * @Description: TODO
 * @date 2019/11/20 22:11
 */
public class OrderEventProducer {

    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public OrderEventProducer(){

    }

    public void  sendData(ByteBuffer bb){
        //1. 发送消息, 首先我们从我们的RingBuffer里面获取一个可用的序号
        long sequence = ringBuffer.next(); //sequence

        try {
            //2. 根据这个序号，找到具体的OrderEvent元素
            //   此时获取的 event 对象，是没有被填充的(属性未被赋值)
            OrderEvent event = ringBuffer.get(sequence);

            //3. 填充对象
            event.setPrice(bb.getLong());
        }finally {
            //4. 提交(发布)操作
            ringBuffer.publish(sequence);
        }

    }
}
