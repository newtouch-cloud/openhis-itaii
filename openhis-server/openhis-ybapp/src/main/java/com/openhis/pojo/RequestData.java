package com.openhis.pojo;

import lombok.Data;

/**
 * 医保入参
 */
@Data
public class RequestData<T> {

    private T data;
    public RequestData(T param) {
            this.data = param;
    }
}
