package com.cpacm.moemusic.ui.beats;

import com.cpacm.core.action.account.AccountDetailAction;
import com.cpacm.core.bean.AccountBean;
import com.cpacm.core.mvp.presenters.BeatsIPresenter;
import com.cpacm.core.mvp.views.BeatsIView;

/**
 * @author: cpacm
 * @date: 2016/7/7
 * @desciption: 主页逻辑处理
 */
public class BeatsPresenter implements BeatsIPresenter {

    private BeatsIView beatsIView;
    private AccountDetailAction detailAction;

    public BeatsPresenter(BeatsIView beatsIView) {
        this.beatsIView = beatsIView;
        detailAction = new AccountDetailAction(this);
    }

    public void getAccountDetail() {
        detailAction.getAccount();
    }

    @Override
    public void setUserDetail(AccountBean accountBean) {
        beatsIView.setUserDetail(accountBean);
    }

    @Override
    public void getUserFail(String msg) {
        beatsIView.getUserFail(msg);
    }
}
