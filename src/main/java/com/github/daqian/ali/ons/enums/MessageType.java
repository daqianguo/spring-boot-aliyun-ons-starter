package com.github.daqian.ali.ons.enums;

/**
 * 消息类型
 *
 * @author daqian
 * @date 2019/3/21 14:58
 */
public enum MessageType {

    NORMAL(1, "普通消息"),
    DELAY(2, "延迟消息"),
    TIMER(3, "定时消息"),
    ORDER(4, "顺序消息"),
    TRANSACTION(5, "事务消息");

    private int code;
    private String name;

    MessageType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return name;
    }

}
