package org.disruptor.corelinks;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * @author Kevin
 * @Title: TradeEventHandler1
 * @ProjectName disruptor-study
 * @Description: TODO
 * @date 2019/11/21 15:42
 */
public class TradeEventHandler4 implements EventHandler<TradeEvent>, WorkHandler<TradeEvent> {
    @Override
    public void onEvent(TradeEvent event, long sequence, boolean b) throws Exception {
        this.onEvent(event);
    }

    public void onEvent(TradeEvent event) throws Exception {
        event.setName("keyboard");
        System.out.println("handler4-set name");
    }
}
