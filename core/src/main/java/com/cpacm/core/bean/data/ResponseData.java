package com.cpacm.core.bean.data;

import com.cpacm.core.bean.InformationBean;

/**
 * @author: cpacm
 * @date: 2016/7/7
 * @desciption:
 */
public class ResponseData<T> {

    private InformationBean<T> information;

    public InformationBean<T> getInformation() {
        return information;
    }

    public void setInformation(InformationBean<T> information) {
        this.information = information;
    }
}
