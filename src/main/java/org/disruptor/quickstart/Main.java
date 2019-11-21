package org.disruptor.quickstart;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Kevin
 * @Title: Main
 * @ProjectName disruptor-study
 * @Description: TODO
 * @date 2019/11/20 19:38
 */
public class Main {
    public static void main(String[] args) {
        OrderEventFactory factory = new OrderEventFactory();
        int ringBufferSize = 1024 * 1024;
        //availableProcessors 方法返回到虚拟机的最大可用的处理器数量;决不会小于一个
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        /**
         * 参数:
         *  1. 消息(event)工厂对象
         *  2. 容器的长度
         *  3. 线程池(再生产中强制使用自定义线程池) RejectedExecutionHandler
         *  4. 单生产者/多生成者
         *  5. 等待策略(此处用的阻塞队列)
         */
        //1. 实例化disruptor对象
        Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(factory, ringBufferSize, executor, ProducerType.SINGLE, new BlockingWaitStrategy());

        //2. 添加消费者的监听(注册消费者)
        disruptor.handleEventsWith(new OrderEventHandler());

        //3. 启动disruptor
        disruptor.start();

        //4. 编写生产者，向disruptor投递消息
        //4.1 获取实际存储数组的容器：RingBuffer
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        //4.2 创建生产者
        OrderEventProducer producer = new OrderEventProducer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long i = 0; i < 100; i++) {
            bb.putLong(0, i);
            producer.sendData(bb);
        }

        disruptor.shutdown();
        executor.shutdown();
    }
}
