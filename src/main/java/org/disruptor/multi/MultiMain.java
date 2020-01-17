package org.disruptor.multi;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author : Kevin
 * @Title : MultiMain
 * @ProjectName disruptor-study
 * @Description : 多生产多消费模式
 * @Time : Created in 2020/1/17 22:54
 * @Modifyed By :
 */
public class MultiMain {
    public static void main(String[] args) {
        // 1.创建一个RingBuffer
        RingBuffer<OrderEvent> ringBuffer = RingBuffer.
                create(ProducerType.MULTI, ()->{return new OrderEvent();},
                        1024 * 1024, new YieldingWaitStrategy());

        // 2.通过RingBuffer创建一个序号屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        // 3.构建多消费者 - 10个长度
        Conmuser[] conmusers = new Conmuser[10];
        for (int i = 0; i < conmusers.length; i++) {
            conmusers[i] = new Conmuser("conmuser" + i);
        }

        // 4.构建多消费者工作池
        WorkerPool<OrderEvent> workerPool =
                new WorkerPool<OrderEvent>(ringBuffer, sequenceBarrier, new ExceptionHandler(), conmusers);

        // 5.设置多个消费者的sequence序号 用于单独统计消费进度, 并设置到RingBuffer中
        // workerPool.getWorkerSequences() 获得消费者 每一个Sequence
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        // 6.启动WorkerPool
        // 在生产环境环境中 一定要使用自定义线程池 -- 例如要设置有界队列
        workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

        CountDownLatch latch = new CountDownLatch(1);

        // 创建100个生产者线程
        for (int i = 0; i < 100; i++) {
            // 生产者用于发布消息,生产投递消息的方式有两种 - Disputor和RingBuffer 此处现在RingBuffer 所以构造参数选择 RingBuffer
            Producer producer = new Producer(ringBuffer);
            // 100个生产者并行投递 所以 需要为每一个 生产者 开启一个线程
            new Thread(()->{
                try {
                    // 此处先等待 等所有的生产者线程都被 准备好。才放开进行生产者的消息投递
                    latch.await();
                    // 每个消费者 投递100条消息
                    for (int j = 0; j < 100; j++) {
                        producer.sendData(UUID.randomUUID().toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        }

        // 等待100个线程完毕
        

    }
}

class EventExceptionHandler implements ExceptionHandler<OrderEvent>{

    @Override
    public void handleEventException(Throwable ex, long sequence, OrderEvent event) {

    }

    @Override
    public void handleOnStartException(Throwable ex) {

    }

    @Override
    public void handleOnShutdownException(Throwable ex) {

    }
}