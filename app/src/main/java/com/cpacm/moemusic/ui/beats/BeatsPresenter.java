package com.cpacm.moemusic.ui.beats;

import com.cpacm.core.action.AccountDetailAction;
import com.cpacm.core.action.PlayListAction;
import com.cpacm.core.bean.AccountBean;
import com.cpacm.core.bean.Song;
import com.cpacm.core.cache.SettingManager;
import com.cpacm.core.db.dao.AccountDao;
import com.cpacm.core.http.HttpUtil;
import com.cpacm.core.mvp.presenters.BeatsIPresenter;
import com.cpacm.core.mvp.presenters.PlaylistIPresenter;
import com.cpacm.core.mvp.views.BeatsIView;
import com.cpacm.moemusic.MoeApplication;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/7/7
 * @desciption: 主页逻辑处理
 */
public class BeatsPresenter implements BeatsIPresenter, PlaylistIPresenter {

    private static final int AUTH_LOGIN_COUNT = 3;

    private BeatsIView beatsIView;
    private AccountDetailAction detailAction;
    private PlayListAction playListAction;

    private int tryLogin = 0;

    public BeatsPresenter(BeatsIView beatsIView) {
        this.beatsIView = beatsIView;
        detailAction = new AccountDetailAction(this);
    }

    public void getAccountDetail() {
        detailAction.getAccount();
    }

    public void requestSongs() {
        playListAction = new PlayListAction(this);
        playListAction.getPlayList();
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
        if (msg.equals(HttpUtil.UNAUTHORIZED) && tryLogin++ < AUTH_LOGIN_COUNT) {
            getAccountDetail();
        } else {
            beatsIView.getUserFail(msg);
        }
    }

    @Override
    public void getPlayList(List<Song> songs) {
        beatsIView.getRandomSongs(songs);
    }

    @Override
    public void getPlaylistFail(String msg) {
        beatsIView.getSongsFail(msg);
    }
}
