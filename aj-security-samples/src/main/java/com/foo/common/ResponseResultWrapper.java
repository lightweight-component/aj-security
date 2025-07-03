package com.foo.common;

import com.ajaxjs.security.encryptedbody.IResponseResult;
import lombok.Data;

@Data
public class ResponseResultWrapper implements IResponseResult {
    private Integer status;

    private String errorCode;

    private String message;

    private Object data;
}
