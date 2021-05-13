package com.ymy.boot.entity;

import lombok.Getter;
import lombok.ToString;

/**
 * 返回给前端的Bean
 *
 * @author Ringo
 * @date 2021/5/12 0:34
 */
@Getter
@ToString
public class RespBean {

    private final int statusCode;     // 状态码

    private final String message;     // 提示消息

    private final Object data;        // 返回的数据

    public RespBean(int statusCode, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int statusCode;

        private String message;

        private Object data;

        private Builder() {
        }

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public Builder ok() {
            this.statusCode = 200;
            return this;
        }

        public Builder failure() {
            this.statusCode = 500;
            return this;
        }

        public RespBean build() {
            return new RespBean(statusCode, message, data);
        }
    }
}
