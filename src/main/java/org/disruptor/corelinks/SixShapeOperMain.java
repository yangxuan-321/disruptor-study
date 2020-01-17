package org.disruptor.corelinks;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Kevin
 * @Title: SixShapeOperMain
 * @ProjectName disruptor-study
 * @Description: TODO
 * @date 2020/1/15 14:34
 */
public class SixShapeOperMain {
    public static void main(String[] args) throws Exception{
        long start = System.currentTimeMillis();

        // es1 生产者线程 -- 用于提交任务
        // 设置成 1 都没问题 因为后面 我们异步的 提交了 一个任务
        ExecutorService es1 = Executors.newFixedThreadPool(4);
        // es2 消费者线程 --
        // 因为 是单消费者模式 况且 有 h1~h5 五个消费者监听 所以 此处的线程池 最好就是五个数目
        // 但是 实际问题 中 的handler 数目不确定, 所以这样做 是不行的
        // 所以 需要 采用 多消费者模式
        ExecutorService es2 = Executors.newFixedThreadPool(5);

        //1.构建disruptor
        // 单生产者 - 依赖于ProducerType.SINGLE
        // 多生产者 - 依赖于ProducerType.MULTI
        Disruptor<TradeEvent> disruptor = new Disruptor<TradeEvent>(
                new TradeEventFactory(),
                1024 * 1024,
                es2,
                ProducerType.SINGLE,
                new BusySpinWaitStrategy()
        );

        // 多消费者需要依赖于 WorkerPool

        //2.绑定消费者
        //六边形操作
        EventHandler h1 = new TradeEventHandler1();
        EventHandler h2 = new TradeEventHandler2();
        EventHandler h3 = new TradeEventHandler3();
        EventHandler h4 = new TradeEventHandler4();
        EventHandler h5 = new TradeEventHandler5();
        // 2.1.开始 h1和h4是并行
        disruptor.handleEventsWith(h1, h4);
        // 2.2.设置h1执行完成之后和h2是串行
        disruptor.after(h1).handleEventsWith(h2);
        // 2.3.设置h4执行完成之后和h5是串行
        disruptor.after(h4).handleEventsWith(h5);
        // 2.4.做汇总(当h2和h5都执行完成之后和h3是串行)
        disruptor.after(h2, h5).handleEventsWith(h3);


        //3.启动disruptor
        RingBuffer<TradeEvent> ringBuffer = disruptor.start();

        CountDownLatch latch = new CountDownLatch(1);
        es1.submit(new TradeEventPublisher(latch, disruptor));

        latch.await();

        disruptor.shutdown();
        es1.shutdown();
        es2.shutdown();

        System.out.println(String.format("总耗时:%d", System.currentTimeMillis() - start));
    }
}
