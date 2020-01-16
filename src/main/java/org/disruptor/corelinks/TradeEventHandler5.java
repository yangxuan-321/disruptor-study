package org.disruptor.corelinks;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import java.util.UUID;

/**
 * @author Kevin
 * @Title: TradeEventHandler1
 * @ProjectName disruptor-study
 * @Description: TODO
 * @date 2019/11/21 15:42
 */
public class TradeEventHandler5 implements EventHandler<TradeEvent>, WorkHandler<TradeEvent> {
    @Override
    public void onEvent(TradeEvent event, long sequence, boolean b) throws Exception {
        this.onEvent(event);
    }

    @Override
    public void onEvent(TradeEvent event) throws Exception {
        Thread.sleep(1000);
        event.setId(UUID.randomUUID().toString());
        System.out.println("handler5-set id");
    }
}
