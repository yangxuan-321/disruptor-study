package org.disruptor.corelinks;

import com.lmax.disruptor.EventFactory;

/**
 * @author Kevin
 * @Title: TradeEventFactory
 * @ProjectName disruptor-study
 * @Description: TODO
 * @date 2019/11/21 15:04
 */
public class TradeEventFactory implements EventFactory<TradeEvent> {
    @Override
    public TradeEvent newInstance() {
        return new TradeEvent();
    }
}
