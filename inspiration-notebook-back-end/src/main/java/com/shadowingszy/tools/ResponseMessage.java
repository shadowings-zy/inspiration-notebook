package com.shadowingszy.tools;

import com.alibaba.fastjson.JSONObject;

public class ResponseMessage {
    private String code;
    private String msg;
    private Object data;

    public ResponseMessage(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public static String toJSONString(ResponseMessage response) {
        Object javaJSONString = JSONObject.toJSON(response);
        return javaJSONString.toString();
    }
}
