package com.github.mars05.crud.intellij.plugin.rpc.response;

import com.alibaba.fastjson.JSON;

/**
 * @author xiaoyu
 */
public abstract class Response {
    private String body;
    private String message;
    private String code;
    private JSON data;

    public void setData(JSON data) {
        this.data = data;
    }

    public JSON getData() {
        return data;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return "0".equals(code);
    }
}
