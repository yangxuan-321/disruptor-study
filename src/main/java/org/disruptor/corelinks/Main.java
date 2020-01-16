package org.disruptor.corelinks;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
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
        //EventHandlerGroup<TradeEvent> handlerGroup = disruptor.handleEventsWith(new TradeEventHandler1())
        //                                                      .handleEventsWith(new TradeEventHandler2())
        //                                                      .handleEventsWith(new TradeEventHandler3());

        // 2.2并行操作 有两种方式
        // 第一种：添加多个handler(非链式操作)
        //disruptor.handleEventsWith(new TradeEventHandler1());
        //disruptor.handleEventsWith(new TradeEventHandler2());
        //disruptor.handleEventsWith(new TradeEventHandler3());
        // 第二种：传多个 handler 来实现并行操作
        // disruptor.handleEventsWith(new TradeEventHandler1(), new TradeEventHandler2(), new TradeEventHandler3());

        // 2.2菱形操作
        // 先并行执行handler1,handler2, 等好h1和h2执行完之后, 然后串行执行
        disruptor.handleEventsWith(new TradeEventHandler1(), new TradeEventHandler2())
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
