package org.disruptor.corelinks;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author Kevin
 * @Title: TradeEventPublish
 * @ProjectName disruptor-study
 * @Description: TODO
 * @date 2019/11/21 15:22
 */
public class TradeEventPublisher implements Runnable {

    private CountDownLatch latch;
    private Disruptor disruptor;

    private static final int PUBLISH_COUNT = 1;

    public TradeEventPublisher(CountDownLatch latch, Disruptor<TradeEvent> disruptor) {
        this.latch = latch;
        this.disruptor = disruptor;
    }

    @Override
    public void run() {
        try {
            // 发布订单
            // 重复10次
            for (int i = 0; i < PUBLISH_COUNT; i++) {
                disruptor.publishEvent(new TradeEventTranslator());
            }
        }finally {
            latch.countDown();
        }

    }
}


class TradeEventTranslator implements EventTranslator<TradeEvent>{

    private Random random = new Random();

    @Override
    public void translateTo(TradeEvent tradeEvent, long sequence) {
        this.generateTradeEvent(tradeEvent);
    }

    private void generateTradeEvent(TradeEvent event){
        event.setPrice(random.nextDouble() * 9999);
    }
}