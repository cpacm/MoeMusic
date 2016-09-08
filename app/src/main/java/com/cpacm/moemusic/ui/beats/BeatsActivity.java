package com.cpacm.moemusic.ui.beats;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cpacm.core.bean.AccountBean;
import com.cpacm.core.bean.Song;
import com.cpacm.core.mvp.views.BeatsIView;
import com.cpacm.moemusic.MoeApplication;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.music.MusicPlaylist;
import com.cpacm.moemusic.music.OnSongChangedListener;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.adapters.BeatsFragmentAdapter;
import com.cpacm.moemusic.ui.music.SongPlayerActivity;
import com.cpacm.moemusic.ui.widgets.CircleImageView;
import com.cpacm.moemusic.ui.widgets.floatingmusicmenu.FloatingMusicMenu;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class BeatsActivity extends AbstractAppActivity implements NavigationView.OnNavigationItemSelectedListener, BeatsIView, OnSongChangedListener {

    private DrawerLayout drawerLayout;
    private BeatsPresenter beatsPresenter;
    private NavigationView navigationView;
    private CircleImageView avatar;
    private CircleImageView userImg;
    private TextView nicknameTv, aboutTv;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //FloatingMusicButton
    private FloatingMusicMenu musicMenu;
    private FloatingActionButton playingBtn, modeBtn, detailBtn, nextBtn;
    private Subscription progressSub;
    private Song curSong;

    private BeatsFragmentAdapter beatsFragmentAdapter;
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beats);
        beatsPresenter = new BeatsPresenter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer_home);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View shadowView = findViewById(R.id.toolbar_shadow);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shadowView.setVisibility(View.GONE);
        }

        View iconLayout = findViewById(R.id.icon_layout);
        iconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        beatsFragmentAdapter = new BeatsFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(beatsFragmentAdapter);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(beatsFragmentAdapter.getCount());
        tabLayout.setupWithViewPager(viewPager);

        initDrawer();
        tryGetData();

        initFloatingMusicButton();
    }

    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_string, R.string.close_string);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        userImg = (CircleImageView) findViewById(R.id.user_icon);

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        avatar = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.drawer_avatar);
        nicknameTv = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_nickname);
        aboutTv = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_about);
    }

    private void initData(AccountBean accountBean) {
        if (accountBean == null) return;
        Glide.with(this)
                .load(accountBean.getUser_avatar().getMedium())
                .placeholder(R.drawable.ic_navi_user)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        userImg.setImageDrawable(resource);
                    }
                });
        Glide.with(this)
                .load(accountBean.getUser_avatar().getLarge())
                .placeholder(R.drawable.ic_navi_user)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        avatar.setImageDrawable(resource);
                    }
                });
        String nickname = accountBean.getUser_nickname();
        if (TextUtils.isEmpty(nickname)) {
            nickname = accountBean.getUser_name();
        }
        nicknameTv.setText(nickname);
        if (TextUtils.isEmpty(accountBean.getAbout())) {
            aboutTv.setVisibility(View.GONE);
        } else {
            aboutTv.setVisibility(View.VISIBLE);
            aboutTv.setText(accountBean.getAbout());
        }
    }

    private void tryGetData() {
        beatsPresenter.getAccountDetail();
    }

    private void initFloatingMusicButton() {
        MusicPlayerManager.get().registerListener(this);
        musicMenu = (FloatingMusicMenu) findViewById(R.id.fmm);
        playingBtn = (FloatingActionButton) findViewById(R.id.fab_play);
        playingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PLAYING) {
                    MusicPlayerManager.get().pause();
                } else {
                    MusicPlayerManager.get().play();
                }
            }
        });
        modeBtn = (FloatingActionButton) findViewById(R.id.fab_mode);
        setPlayMode(MusicPlayerManager.get().getPlayMode());
        modeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int playMode = MusicPlayerManager.get().switchPlayMode();
                setPlayMode(playMode);
            }
        });
        nextBtn = (FloatingActionButton) findViewById(R.id.fab_next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MusicPlayerManager.get().getMusicPlaylist() != null)
                    MusicPlayerManager.get().playNext();
                else {
                    showToast(R.string.music_playlist_next_null);
                }
            }
        });
        detailBtn = (FloatingActionButton) findViewById(R.id.fab_player);
        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MusicPlayerManager.get().getMusicPlaylist() == null) {
                    beatsPresenter.requestSongs();
                    showIndeterminateProgressDialog(true);
                } else {
                    gotoSongPlayerActivity();
                }
                musicMenu.collapse();
            }
        });
        updateProgress();
        updateSong(MusicPlayerManager.get().getPlayingSong());
    }

    private void showIndeterminateProgressDialog(boolean horizontal) {
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.music_explore_title)
                .content(R.string.music_explore_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(horizontal)
                .show();
    }

    private void updateProgress() {
        progressSub = Observable.interval(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        float progress = MusicPlayerManager.get().getCurrentPosition() * 1.0f / MusicPlayerManager.get().getCurrentMaxDuration() * 100;
                        musicMenu.setProgress(progress);
                    }
                });
    }

    public void setPlayMode(int playMode) {
        if (playMode == MusicPlayerManager.CYCLETYPE) {
            //showToast(R.string.music_mode_cycle);
            modeBtn.setImageResource(R.drawable.ic_play_repeat);
        } else if (playMode == MusicPlayerManager.SINGLETYPE) {
            modeBtn.setImageResource(R.drawable.ic_play_repeat_one);
            //showToast(R.string.music_mode_single);
        } else if (playMode == MusicPlayerManager.RANDOMTYPE) {
            modeBtn.setImageResource(R.drawable.ic_play_shuffle);
            //showToast(R.string.music_mode_random);
        }
    }

    public boolean gotoSongPlayerActivity() {
        if (MusicPlayerManager.get().getPlayingSong() == null) {
            showToast(R.string.music_playing_none);
            return false;
        }
        SongPlayerActivity.open(this);
        return true;
    }

    @Override
    public void onSongChanged(Song song) {
        updateSong(song);
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {
        updatePlayStatus();
    }

    private void updatePlayStatus() {
        if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PLAYING) {
            playingBtn.setImageResource(R.drawable.ic_play);
            musicMenu.rotateStart();
        } else if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PAUSED) {
            playingBtn.setImageResource(R.drawable.ic_pause);
            musicMenu.rotateStop();
        }
    }

    private void updateSong(Song song) {
        if (song == null) {
            musicMenu.rotateStop();
            return;
        }
        curSong = song;
        if (!TextUtils.isEmpty(song.getCoverUrl())) {
            Glide.with(this)
                    .load(song.getCoverUrl())
                    .asBitmap()
                    .placeholder(R.drawable.moefou)
                    .error(R.drawable.moefou)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            musicMenu.setMusicCover(resource);
                            musicMenu.rotateStart();
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers();
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_playing) {
            if (!gotoSongPlayerActivity())
                return true;
            // Handle the camera action
        } else if (id == R.id.nav_playlist) {

        } else if (id == R.id.nav_download) {

        } else if (id == R.id.nav_library) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_feedback) {

        }
        //关闭侧滑栏
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        if (MusicPlayerManager.get().getState() == PlaybackStateCompat.STATE_PLAYING) {
            if (curSong != MusicPlayerManager.get().getPlayingSong()) {
                updateSong(MusicPlayerManager.get().getPlayingSong());
            }

            musicMenu.rotateStart();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        musicMenu.rotateStop();
        super.onPause();
    }

    @Override
    public void getRandomSongs(List<Song> songs) {
        if (dialog != null) {
            dialog.dismiss();
        }
        MusicPlaylist musicPlaylist = new MusicPlaylist(songs);
        musicPlaylist.setTitle(getString(R.string.music_find));
        MusicPlayerManager.get().playQueue(musicPlaylist, 0);
        gotoSongPlayerActivity();
    }

    @Override
    public void getSongsFail(String msg) {
        if (dialog != null) {
            dialog.dismiss();
        }
        showToast(R.string.music_explore_fail);
    }

    @Override
    public void setUserDetail(AccountBean accountBean) {
        initData(accountBean);
    }

    @Override
    public void getUserFail(String msg) {
        initData(MoeApplication.getInstance().getAccountBean());
        showSnackBar(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayerManager.get().unregisterListener(this);
        progressSub.unsubscribe();
    }
}
