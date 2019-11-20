package org.disruptor.quickstart;

import com.lmax.disruptor.EventFactory;

/**
 * @author Kevin
 * @Title: OrderEventFactory
 * @ProjectName disruptor-study
 * @Description: 订单工厂 用于创建一个个订单对象
 * @date 2019/11/20 16:48
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        // 返回空的数据对象(event)
        return new OrderEvent();
    }
}
