package com.cpacm.moemusic.presenter;


import com.cpacm.core.action.HitokotoAction;
import com.cpacm.core.mvp.presenters.HitokotoIPresenter;
import com.cpacm.core.mvp.views.HitokotoIView;

/**
 * @auther: cpacm
 * @date: 2016/6/30
 * @desciption: 一言业务处理
 */
public class HitokotoPresenter implements HitokotoIPresenter {

    private HitokotoIView hitokotoIView;
    private HitokotoAction hitokotoAction;

    public HitokotoPresenter(HitokotoIView hitokotoIView) {
        this.hitokotoIView = hitokotoIView;
    }

    public void getKoto() {
        hitokotoAction = new HitokotoAction(this);
        hitokotoAction.getRandKoto("a");
    }

    @Override
    public void randKoto(String hitokoto) {
        hitokotoIView.randKoto(hitokoto);
    }
}
