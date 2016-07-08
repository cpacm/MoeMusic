package com.cpacm.moemusic.ui.beats;

import com.cpacm.core.action.account.AccountDetailAction;
import com.cpacm.core.bean.AccountBean;
import com.cpacm.core.cache.SettingManager;
import com.cpacm.core.db.dao.AccountDao;
import com.cpacm.core.mvp.presenters.BeatsIPresenter;
import com.cpacm.core.mvp.views.BeatsIView;
import com.cpacm.moemusic.MoeApplication;

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
        SettingManager.getInstance().setSetting(SettingManager.ACCOUNT_ID, accountBean.getUid());
        MoeApplication.getInstance().setAccountBean(accountBean);
        AccountDao accountDao = new AccountDao();
        accountDao.updateAccount(accountBean);
        accountDao.close();
    }

    @Override
    public void getUserFail(String msg) {
        beatsIView.getUserFail(msg);
    }
}
