package org.disruptor.quickstart;


import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.EventHandler;

/**
 * @author Kevin
 * @Title: OrderEventHandler
 * @ProjectName disruptor-study
 * @Description: 事件处理器
 * @date 2019/11/20 16:51
 */
public class OrderEventHandler implements EventHandler<OrderEvent> {

    /**
     *  参数：
     *      1. 具体的事件
     *      2. TODO
     *      3. TODO
     */

    @Override
    public void onEvent(OrderEvent orderEvent, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(String.format("消费者:%s", JSON.toJSON(orderEvent)));
    }
}
