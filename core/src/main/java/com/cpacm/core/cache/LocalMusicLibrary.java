package com.cpacm.core.cache;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.cpacm.core.bean.Album;
import com.cpacm.core.bean.Artist;
import com.cpacm.core.bean.Song;
import com.cpacm.core.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/9/22.
 * @description: 本地音乐库
 */

public class LocalMusicLibrary {


    /*######################### song ###########################*/

    /**
     * 获取本地所有的歌曲
     *
     * @param context
     * @return
     */
    public static List<Song> getAllSongs(Context context) {
        List<Song> songs = new ArrayList<>();
        String selectionStatement = "is_music=1 AND title != ''";
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id", "_data"}, selectionStatement, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                Song song = getSongFromCursor(cursor);
                if (song.isStatus()) {
                    songs.add(song);
                }
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return songs;
    }

    public static Song getSongFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
        String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
        String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
        int trackNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
        long artistId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
        long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
        String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        Song song = new Song();
        song.setId(-id);
        song.setTitle(title);
        song.setAlbumName(album);
        song.setArtistName(artist);
        song.setDuration(duration);
        song.setTrackNumber(trackNumber);
        song.setArtistId(artistId);
        song.setAlbumId(albumId);
        String cover = getAlbumArtUri(albumId).toString();
        song.setCoverUrl(cover);
        song.setPath(url);
        song.setUrl(url);
        if (FileUtils.existFile(url)) {
            song.setStatus(true);
        }
        return song;
    }

    /*######################### album ###########################*/

    /**
     * 获取album的封面照片
     *
     * @param paramInt
     * @return
     */
    private static Uri getAlbumArtUri(long paramInt) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), paramInt);
    }

    private static Album getAlbum(Cursor cursor) {
        Album album = new Album();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                album = getAlbumFromCursor(cursor);
            }
        }
        if (cursor != null)
            cursor.close();
        return album;
    }

    private static Album getAlbumFromCursor(Cursor cursor) {
        long _id = cursor.getLong(0);
        Uri cover = getAlbumArtUri(_id);
        String coverUrl = cover == null ? null : cover.toString();
        return new Album(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4), cursor.getInt(5), coverUrl);
    }


    private static List<Album> getAlbumsForCursor(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                arrayList.add(getAlbumFromCursor(cursor));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static List<Album> getAllAlbums(Context context) {
        return getAlbumsForCursor(makeAlbumCursor(context, null, null));
    }

    public static Album getAlbum(Context context, long id) {
        return getAlbum(makeAlbumCursor(context, "_id=?", new String[]{String.valueOf(id)}));
    }

    public static List<Album> getAlbums(Context context, String paramString) {
        return getAlbumsForCursor(makeAlbumCursor(context, "album LIKE ?", new String[]{"%" + paramString + "%"}));
    }

    private static Cursor makeAlbumCursor(Context context, String selection, String[] paramArrayOfString) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{"_id", "album", "artist", "artist_id", "numsongs", "minyear"}, selection, paramArrayOfString, null);
        return cursor;
    }

    public static ArrayList<Song> getSongsForAlbum(Context context, long albumID) {
        Cursor cursor = makeAlbumSongCursor(context, albumID);
        ArrayList<Song> arrayList = new ArrayList<>();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                arrayList.add(getSongFromCursor(cursor));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static Cursor makeAlbumSongCursor(Context context, long albumID) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String string = "is_music=1 AND title != '' AND album_id=" + albumID;
        Cursor cursor = contentResolver.query(uri, new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id", "_data"}, string, null, null);
        return cursor;
    }
    /*######################### artist ###########################*/

    private static Artist getArtist(Cursor cursor) {
        Artist artist = new Artist();
        if (cursor != null) {
            if (cursor.moveToFirst())
                artist = new Artist(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
        }
        if (cursor != null)
            cursor.close();
        return artist;
    }

    private static List<Artist> getArtistsForCursor(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                arrayList.add(new Artist(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3)));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static List<Artist> getAllArtists(Context context) {
        return getArtistsForCursor(makeArtistCursor(context, null, null));
    }

    public static Artist getArtist(Context context, long id) {
        return getArtist(makeArtistCursor(context, "_id=?", new String[]{String.valueOf(id)}));
    }

    public static List<Artist> getArtists(Context context, String paramString) {
        return getArtistsForCursor(makeArtistCursor(context, "artist LIKE ?", new String[]{"%" + paramString + "%"}));
    }

    private static Cursor makeArtistCursor(Context context, String selection, String[] paramArrayOfString) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, new String[]{"_id", "artist", "number_of_albums", "number_of_tracks"}, selection, paramArrayOfString, null);
        return cursor;
    }

    public static ArrayList<Song> getSongsForArtist(Context context, long artistID) {
        Cursor cursor = makeArtistSongCursor(context, artistID);
        ArrayList songsList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                songsList.add(getSongFromCursor(cursor));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return songsList;
    }


    private static Cursor makeArtistSongCursor(Context context, long artistID) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String string = "is_music=1 AND title != '' AND artist_id=" + artistID;
        return contentResolver.query(uri, new String[]{"_id", "title", "artist", "album", "duration", "track", "album_id"}, string, null, null);
    }

}
