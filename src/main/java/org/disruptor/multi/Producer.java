package org.disruptor.multi;

import com.lmax.disruptor.RingBuffer;
import org.springframework.core.annotation.Order;

/**
 * @author : Kevin
 * @Title : Producer
 * @ProjectName disruptor-study
 * @Description : TODO
 * @Time : Created in 2020/1/18 1:21
 * @Modifyed By :
 */
public class Producer {

    private RingBuffer<OrderEvent> ringBuffer;

    public Producer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(String uuid) {
        // 获得一个sequence
        long sequence = ringBuffer.next();

        try {
            // 通过sequence拿到一个具体的 Order对象 但是 只是 被 new 出来的。未做过 任何赋值
            OrderEvent orderEvent = ringBuffer.get(sequence);

            // 设置数据
            orderEvent.setId(uuid);
        }finally {
            //投递发布一个事件 - 让消费者 接到
            ringBuffer.publish(sequence);
        }
    }
}
