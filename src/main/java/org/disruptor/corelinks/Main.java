package org.disruptor.corelinks;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Kevin
 * @Title: Main
 * @ProjectName disruptor-study
 * @Description: TODO
 * @date 2019/11/21 14:56
 */
public class Main {

    // 用于自己提交任务的线程池
    public static final ExecutorService excutors = Executors.newFixedThreadPool(8);

    public static void main(String[] args) throws Exception{

        long start = System.currentTimeMillis();

//        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ExecutorService es = Executors.newFixedThreadPool(8);

        //1.构建disruptor
        Disruptor<TradeEvent> disruptor = new Disruptor<TradeEvent>(
                new TradeEventFactory(),
                1024 * 1024,
                es,
                ProducerType.SINGLE,
                new BusySpinWaitStrategy()
        );

        //2.绑定消费者
        //2.1串行操作
        EventHandlerGroup<TradeEvent> handlerGroup = disruptor.handleEventsWith(new TradeEventHandler1())
                                                              .handleEventsWith(new TradeEventHandler2())
                                                              .handleEventsWith(new TradeEventHandler3());

        //3.启动disruptor
        RingBuffer<TradeEvent> ringBuffer = disruptor.start();

        CountDownLatch latch = new CountDownLatch(1);
        excutors.submit(new TradeEventPublisher(latch, disruptor));

        latch.await();

        disruptor.shutdown();
        es.shutdown();
        excutors.shutdown();

        System.out.println(String.format("总耗时:%d", System.currentTimeMillis() - start));
    }
}