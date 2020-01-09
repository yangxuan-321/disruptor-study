package org.disruptor.corelinks;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import java.text.MessageFormat;

/**
 * @author Kevin
 * @Title: TradeEventHandler1
 * @ProjectName disruptor-study
 * @Description: TODO
 * @date 2019/11/21 15:42
 */
public class TradeEventHandler3 implements EventHandler<TradeEvent>, WorkHandler<TradeEvent> {
    @Override
    public void onEvent(TradeEvent event, long sequence, boolean b) throws Exception {
        this.onEvent(event);
    }

    @Override
    public void onEvent(TradeEvent event) throws Exception {
        Thread.sleep(1000);
        System.out.println(MessageFormat.format("handler3-print:{0}.", JSON.toJSON(event)));
    }
}
