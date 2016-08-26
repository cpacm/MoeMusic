package com.cpacm.moemusic.ui.music;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.cpacm.core.bean.Song;
import com.cpacm.core.utils.FileUtils;
import com.cpacm.moemusic.R;
import com.cpacm.moemusic.music.MusicPlayerManager;
import com.cpacm.moemusic.ui.AbstractAppActivity;

/**
 * @author: cpacm
 * @date: 2016/8/24
 * @desciption: 播放器界面
 */
public class SongPlayerActivity extends AbstractAppActivity {

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SongPlayerActivity.class);
        context.startActivity(intent);
    }

    private GLAudioVisualizationView visualizationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String path = FileUtils.getSDCardFilePath("xiami/Heaven's Sky.mp3");
        Song song = new Song(1L, "test", 2, "test", 3, "cpacm", Uri.parse(path), 100, 1000, 12512, "320k", 12344, "test", true);
        MusicPlayerManager.get().play(song);

        visualizationView = (GLAudioVisualizationView) findViewById(R.id.visualizer_view);
        visualizationView.linkTo(DbmHandler.Factory.newVisualizerHandler(this, MusicPlayerManager.get().getMediaPlayer().getAudioSessionId()));
    }

    @Override
    public void onResume() {
        super.onResume();
        visualizationView.onResume();
    }

    @Override
    public void onPause() {
        visualizationView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        visualizationView.release();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
