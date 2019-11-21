package org.disruptor.corelinks;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kevin
 * @Title: Trade
 * @ProjectName disruptor-study
 * @Description: 交易 基本对象
 * @date 2019/11/21 14:56
 */
public class TradeEvent {
    private String id;
    private String name;
    private double price;
    private AtomicInteger count = new AtomicInteger(0);
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public AtomicInteger getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
