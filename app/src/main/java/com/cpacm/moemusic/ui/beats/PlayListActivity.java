package com.cpacm.moemusic.ui.beats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cpacm.core.bean.Album;
import com.cpacm.core.bean.CollectionBean;
import com.cpacm.core.bean.Song;
import com.cpacm.core.bean.WikiBean;
import com.cpacm.core.bean.event.CollectionUpdateEvent;
import com.cpacm.core.db.CollectionManager;
import com.cpacm.core.http.RxBus;
import com.cpacm.core.mvp.views.PlayListIView;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.OnSongChangedListener;
import com.cpacm.moemusic.ui.PermissionActivity;
import com.cpacm.moemusic.ui.adapters.CollectionAdapter;
import com.cpacm.moemusic.ui.adapters.OnItemClickListener;
import com.cpacm.moemusic.ui.adapters.PlayListAdapter;
import com.cpacm.moemusic.ui.music.MoeDetailActivity;
import com.cpacm.moemusic.ui.widgets.recyclerview.OnStartDragListener;
import com.cpacm.moemusic.ui.widgets.recyclerview.SimpleItemTouchHelperCallback;

import rx.functions.Action1;

/**
 * @author: cpacm
 * @date: 2016/8/31
 * @desciption: 播放列表
 */
public class PlayListActivity extends PermissionActivity implements OnSongChangedListener, OnStartDragListener, PlayListIView {

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PlayListActivity.class);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private PlayListAdapter playListAdapter;
    private ItemTouchHelper itemTouchHelper;
    private MaterialDialog loadingDialog;

    private PlayListPresenter playListPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MusicPlayerManager.get().registerListener(this);

        playListPresenter = new PlayListPresenter(this);

        initRecyclerView();
    }

    private void initRecyclerView() {
        playListAdapter = new PlayListAdapter(this, this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(playListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (MusicPlayerManager.get().getMusicPlaylist() != null) {
            playListAdapter.setData(MusicPlayerManager.get().getMusicPlaylist().getQueue());
        }
        playListAdapter.setSongClickListener(new OnItemClickListener<Song>() {
            @Override
            public void onItemClick(Song song, int position) {
                MusicPlayerManager.get().playQueueItem(position);
            }

            @Override
            public void onItemSettingClick(View v, Song song, int position) {
                showPopupMenu(v, song, position);
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(playListAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showPopupMenu(final View v, final Song song, final int position) {

        final PopupMenu menu = new PopupMenu(this, v);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        MusicPlayerManager.get().playQueueItem(position);
                        gotoSongPlayerActivity();
                        break;
                    case R.id.popup_song_fav:
                        showCollectionDialog(song);
                        break;
                    case R.id.popup_song_download:
                        downloadSong(v,song);
                        break;
                    case R.id.popup_song_goto_album:
                        playListPresenter.requestAlbum(song);
                        loadingDialog = new MaterialDialog.Builder(PlayListActivity.this)
                                .title(R.string.music_playlist_album)
                                .progress(true, 0)
                                .progressIndeterminateStyle(true)
                                .show();
                        break;
                }
                return false;
            }
        });
        menu.inflate(R.menu.popup_playlist_setting);
        menu.show();
    }

    public void showCollectionDialog(final Song song) {
        CollectionAdapter collectionAdapter = new CollectionAdapter(this, true);
        final MaterialDialog dialog = new MaterialDialog.Builder(PlayListActivity.this)
                .title(R.string.collection_dialog_selection_title)
                .adapter(collectionAdapter, new LinearLayoutManager(this))
                .build();
        collectionAdapter.setItemClickListener(new OnItemClickListener<CollectionBean>() {
            @Override
            public void onItemClick(CollectionBean item, int position) {
                if (item == null) {
                    dialog.dismiss();
                    return;
                }
                CollectionManager.getInstance().insertCollectionShipAsync(item, song, new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        dialog.dismiss();
                        showToast(aBoolean ? R.string.collect_song_success : R.string.collect_song_fail);
                        RxBus.getDefault().post(new CollectionUpdateEvent(aBoolean));//通知首页收藏夹数据变化
                    }
                });
            }

            @Override
            public void onItemSettingClick(View v, CollectionBean item, int position) {

            }
        });
        dialog.show();
    }

    @Override
    public void onSongChanged(Song song) {
        if (MusicPlayerManager.get().getMusicPlaylist() != null) {
            playListAdapter.setData(MusicPlayerManager.get().getMusicPlaylist().getQueue());
        }
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayerManager.get().unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.action_clear) {
            if (playListAdapter.getItemCount() > 0)
                showBasicDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    private void showBasicDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.playlist_dialog_title)
                .content(R.string.playlist_dialog_description)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        playListAdapter.clearAll();
                        MusicPlayerManager.get().clearPlayer();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void getAlbum(boolean moeAlbum, WikiBean wiki, Album album) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        if (moeAlbum) {
            MoeDetailActivity.open(this, wiki);
        } else {
            LocalAlbumDetailActivity.open(this, album);
        }
    }

    @Override
    public void fail(String msg) {
        showSnackBar(recyclerView,R.string.music_playlist_album_fail);
    }
}
