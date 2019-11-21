package org.disruptor.corelinks;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.EventHandler;

import java.util.UUID;

/**
 * @author Kevin
 * @Title: TradeEventHandler1
 * @ProjectName disruptor-study
 * @Description: TODO
 * @date 2019/11/21 15:42
 */
public class TradeEventHandler1 implements EventHandler<TradeEvent> {
    @Override
    public void onEvent(TradeEvent event, long sequence, boolean b) throws Exception {
        event.setName("handler1");
        Thread.sleep(1000);
        System.out.println("handler-set name");
    }
}
