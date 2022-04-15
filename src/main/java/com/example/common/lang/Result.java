package com.example.common.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于异步统一返回的结果封装
 *
 * 是否成功，可用code表示（如200表示成功，400表示异常）
 * 结果消息
 * 结果数据
 *
 * @Data ： 注在类上，提供类的get、set、equals、hashCode、canEqual、toString方法
 */
@Data
public class Result implements Serializable {

    private int code;
    private String msg;
    private Object date;

    public static Result succ(Object data) {
        return succ(200,"操作成功",data);
    }

    public static Result succ(int code, String msg, Object date)
    {
        Result m = new Result();
        m.setCode(code);
        m.setMsg(msg);
        m.setDate(date);
        return m;
    }



    public static Result fail(String msg) {
        return fail(400, msg, null);
    }

    public static Result fail(String msg, Object date) {
        return fail(400, msg, date);
    }

    public static Result fail(int code, String msg, Object date) {
        Result m = new Result();
        m.setCode(code);
        m.setMsg(msg);
        m.setDate(date);
        return m;
    }

}
