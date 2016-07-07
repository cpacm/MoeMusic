package com.cpacm.core.bean.data;

import com.cpacm.core.action.CodeUtil;

/**
 * 通用的接口数据模板<br />
 * common api response template
 *
 * @Auther: cpacm
 * @Date: 2015/10/21 0021-上午 10:05
 */
public class ApiResponse<T> {

    private T response;

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
