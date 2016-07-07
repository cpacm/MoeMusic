package com.cpacm.core.bean.data;

import com.cpacm.core.bean.AccountBean;

/**
 * @author: cpacm
 * @date: 2016/7/7
 * @desciption: 用户信息的数据
 */
public class AccountData extends ResponseData<Object> {
    private AccountBean user;

    public AccountBean getUser() {
        return user;
    }

    public void setUser(AccountBean user) {
        this.user = user;
    }
}
