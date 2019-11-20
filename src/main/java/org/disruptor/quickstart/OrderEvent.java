package org.disruptor.quickstart;

/**
 * @author Kevin
 * @Title: OrderEvent
 * @ProjectName disruptor-study
 * @Description: 模拟订单事件
 * @date 2019/11/20 16:47
 */
public class OrderEvent {
    // 订单金额
    private long price;

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
