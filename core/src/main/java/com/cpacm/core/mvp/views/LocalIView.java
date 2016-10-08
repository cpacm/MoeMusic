package com.cpacm.core.mvp.views;

import com.cpacm.core.bean.Album;
import com.cpacm.core.bean.Artist;
import com.cpacm.core.bean.Song;

import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/10/4
 * @desciption: 本地视图的接口
 */

public interface LocalIView {

    interface LocalMusic{
        void getLocalMusic(List<Song> songs);
    }

    interface LocalAlbum{
        void getLocalAlbum(List<Album> alba);
    }

    interface LocalArtist{
        void getLocalArtist(List<Artist> artists);
    }
}
