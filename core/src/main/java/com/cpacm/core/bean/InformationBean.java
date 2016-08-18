package com.cpacm.core.bean;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/7/7
 * @desciption: 接口返回信息
 */
public class InformationBean<T, K> {

    /**
     * parameters : null
     * msg : []
     * has_error : false
     * error : 0
     * request : /user/detail_json
     * page: 1
     * perpage: 20
     * count: 656
     */

    private T parameters;
    private boolean has_error;
    private int error;
    private String request;
    private K msg;
    private int page;
    private int perpage;
    private int count;

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(T parameters) {
        this.parameters = parameters;
    }

    public boolean isHas_error() {
        return has_error;
    }

    public void setHas_error(boolean has_error) {
        this.has_error = has_error;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public K getMsg() {
        return msg;
    }

    public void setMsg(K msg) {
        this.msg = msg;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
