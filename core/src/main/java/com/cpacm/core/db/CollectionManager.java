package com.cpacm.core.db;

import com.cpacm.core.bean.CollectionBean;
import com.cpacm.core.bean.CollectionShipBean;
import com.cpacm.core.bean.Song;
import com.cpacm.core.bean.event.CollectionUpdateEvent;
import com.cpacm.core.cache.SongManager;
import com.cpacm.core.db.dao.CollectionDao;
import com.cpacm.core.db.dao.CollectionShipDao;
import com.cpacm.core.http.RxBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author: cpacm
 * @date: 2016/9/27
 * @desciption: 收藏夹管理类
 */

public class CollectionManager {

    private static CollectionManager instance;
    private CollectionDao collectionDao;
    private CollectionShipDao collectionShipDao;
    private List<CollectionBean> collectionList;

    public static CollectionManager getInstance() {
        if (instance == null) {
            instance = new CollectionManager();
        }
        return instance;
    }

    public CollectionManager() {
        collectionDao = new CollectionDao();
        collectionShipDao = new CollectionShipDao();
        getAllCollectionsFromDao();
    }

    /**
     * 预先异步从数据库获取数据
     */
    private void getAllCollectionsFromDao() {
        Observable.create(
                new Observable.OnSubscribe<List<CollectionBean>>() {
                    @Override
                    public void call(Subscriber<? super List<CollectionBean>> subscriber) {
                        subscriber.onNext(collectionDao.queryAll());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CollectionBean>>() {
                    @Override
                    public void call(List<CollectionBean> collectionBeen) {
                        collectionList = collectionBeen;
                    }
                });
    }

    public List<CollectionBean> getCollectionList() {
        List<CollectionBean> collectionBeen = new ArrayList<>();
        if (getAllCollections() != null)
            collectionBeen.addAll(getAllCollections());
        return collectionBeen;
    }

    /**
     * 获取所有收藏夹，若内存中没有数据则直接在UI线程上访问数据库获得数据<br />
     * 原则上是要异步获取数据，这里简化了步骤。
     *
     * @return
     */
    private List<CollectionBean> getAllCollections() {
        if (collectionList == null) {
            collectionList = collectionDao.queryAll();
        }
        if (collectionList == null) {
            collectionList = new ArrayList<>();
        }
        return collectionList;
    }

    public CollectionBean getCollectionById(int id) {
        for (CollectionBean bean : getAllCollections()) {
            if (bean.getId() == id) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 删除收藏夹
     *
     * @param bean
     */
    public void deleteCollection(CollectionBean bean) {
        // TODO 要删除adapter的数据
        Observable.just(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<CollectionBean, Integer>() {
                    @Override
                    public Integer call(CollectionBean collectionBean) {
                        int index = containCollection(collectionBean);
                        collectionDao.deleteCollection(collectionBean);
                        return index;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer index) {
                        if (index >= 0) {
                            getAllCollections().remove(index);
                        }
                    }
                });
    }


    /**
     * 设置收藏夹
     *
     * @param bean
     */
    public void setCollection(CollectionBean bean) {
        int index = containCollection(bean);
        if (index < 0) {
            long id = collectionDao.insertCollection(bean);
            bean.setId((int) id);
            getAllCollections().add(bean);
        } else {
            collectionDao.updateCollection(bean);
            getAllCollections().set(index, bean);
        }
    }

    /**
     * 当数据库已经存在该收藏夹时，进行更新<br />
     * 当数据库不存在该收藏夹时，进行插入操作
     *
     * @param bean 收藏夹实例
     */
    public void setCollectionAsync(CollectionBean bean) {
        Observable.just(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<CollectionBean>() {
                    @Override
                    public void call(CollectionBean collectionBean) {
                        setCollection(collectionBean);
                    }
                });
    }

    /**
     * 是否包含当前收藏夹，是返回位置，否返回-1
     *
     * @param collectionBean
     * @return 0 or -1
     */
    private int containCollection(CollectionBean collectionBean) {
        for (CollectionBean bean : getAllCollections()) {
            if (bean.getId() == collectionBean.getId()) {
                int index = getAllCollections().indexOf(bean);
                return index;
            }
        }
        return -1;
    }

    /*###################################  收藏夹关系操作  ######################################*/

    /**
     * 获取某个收藏夹的所有关系
     *
     * @param cid
     * @return
     */
    public List<CollectionShipBean> getCollectionShipList(int cid) {
        List<CollectionShipBean> cs = new ArrayList<>();
        List<CollectionShipBean> collectionShipBeen = collectionShipDao.queryByCid(cid);
        if (collectionShipBeen != null) {
            cs.addAll(collectionShipBeen);
        }
        return cs;
    }

    /**
     * 收藏歌曲
     *
     * @param bean 收藏夹
     * @param song 歌曲
     * @return
     */
    public long insertCollectionShip(CollectionBean bean, Song song) {
        if (SongManager.getInstance().querySong(song.getId()) == null) {
            SongManager.getInstance().insertOrUpdateSong(song);
        }
        CollectionShipBean collectionShipBean = new CollectionShipBean(-1, bean.getId(), (int) song.getId());
        long index = collectionShipDao.insertCollectionShip(collectionShipBean);
        if (index > 0) {
            bean.setCount(bean.getCount() + 1);
            setCollection(bean);
        }
        return index;
    }

    public void insertCollectionShipAsync(final CollectionBean item, final Song song,Action1<Boolean> action1) {
        Observable.create(
                new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        long index = CollectionManager.getInstance().insertCollectionShip(item, song);
                        subscriber.onNext(index > 0);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    /**
     * 删除收藏夹的歌曲
     *
     * @param id
     * @return
     */
    public int deleteCollectionShip(int id) {
        return collectionShipDao.deleteCollection(id);
    }

    public int deleteCollectionShip(CollectionShipBean bean) {
        return collectionShipDao.deleteCollection(bean.getId());
    }

}
