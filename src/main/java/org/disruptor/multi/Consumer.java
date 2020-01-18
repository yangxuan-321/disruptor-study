package org.disruptor.multi;

import com.lmax.disruptor.WorkHandler;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : Kevin
 * @Title : Conmuser
 * @ProjectName disruptor-study
 * @Description : 消费者
 * @Time : Created in 2020/1/17 23:03
 * @Modifyed By :
 */
public class Consumer implements WorkHandler<OrderEvent> {
    // 消费者Id
    private String consumerId;
    // 用于计数统计
    private static AtomicInteger count = new AtomicInteger(0);

    private Random random = new Random();

    public Consumer(String consumerId) {
        this.consumerId = consumerId;
    }

    @Override
    public void onEvent(OrderEvent event) throws Exception {
        // 模拟每做一个任务需要 1~4 毫秒
        Thread.sleep(1 * random.nextInt(5));
        System.out.println(String.format("当前消费者:%s, 消费信息ID:%s", this.consumerId, event.getId()));
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}
