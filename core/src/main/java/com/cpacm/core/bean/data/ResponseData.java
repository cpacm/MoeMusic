package com.cpacm.core.bean.data;

import com.cpacm.core.bean.InformationBean;

/**
 * @author: cpacm
 * @date: 2016/7/7
 * @desciption: T 为parameters对象，K为msg对象
 */
public class ResponseData<T, K> {

    private InformationBean<T, K> information;

    public InformationBean<T, K> getInformation() {
        return information;
    }

    public void setInformation(InformationBean<T, K> information) {
        this.information = information;
    }
}
