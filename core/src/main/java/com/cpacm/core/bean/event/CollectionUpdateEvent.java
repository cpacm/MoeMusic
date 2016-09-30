package com.cpacm.core.bean.event;

/**
 * @author: cpacm
 * @date: 2016/9/28
 * @desciption: 收藏夹更新事件通知
 */

public class CollectionUpdateEvent {
    private boolean update;

    public CollectionUpdateEvent(boolean update) {
        this.update = update;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
