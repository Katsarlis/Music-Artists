package com.sampled.musicartists;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper implements ArtistsListener {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "artistsDB";
    private static final String TABLE_NAME = "artists";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_GENRES = "genres";
    private static final String KEY_TRACKS = "tracks";
    private static final String KEY_ALBUMS = "albums";
    private static final String KEY_LINK = "link";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_SMALL = "small";
    private static final String KEY_BIG = "big";
    private static final String KEY_ISFAV = "isFav";

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            KEY_ID + " integer primary key," +
            KEY_NAME + " text," +
            KEY_GENRES + " text," +
            KEY_TRACKS + " integer," +
            KEY_ALBUMS + " integer," +
            KEY_LINK + " text," +
            KEY_DESCRIPTION + " text," +
            KEY_SMALL + " text," +
            KEY_BIG + " text," +
            KEY_ISFAV + " boolean" +
            ");";
    String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String CHECK_TABLE = "SELECT * FROM " + TABLE_NAME + ";";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }


    @Override
    public void addArtist(Artist artist) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, artist.id);
            values.put(KEY_NAME, artist.name);
            values.put(KEY_GENRES, artist.genres);
            values.put(KEY_TRACKS, artist.tracks);
            values.put(KEY_ALBUMS, artist.albums);
            values.put(KEY_LINK, artist.link);
            values.put(KEY_DESCRIPTION, artist.description);
            values.put(KEY_SMALL, artist.small);
            values.put(KEY_BIG, artist.big);
            values.put(KEY_ISFAV, artist.isFav);
            db.insert(TABLE_NAME, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Artist> getArtists() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Artist> artistsArrayList = null;
        try {
            artistsArrayList = new ArrayList<Artist>();
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    Artist artist = new Artist();
                    artist.genresArray = new ArrayList<>();
                    artist.id = cursor.getInt(0);
                    artist.name = cursor.getString(1);
                    artist.genres = cursor.getString(2);
                    JSONArray jsonArray = new JSONArray(cursor.getString(2));

                    for (int k = 0; k < jsonArray.length(); k++) {
                        artist.genresArray.add(jsonArray.getString(k));
                    }
                    artist.tracks = cursor.getInt(3);
                    artist.albums = cursor.getInt(4);
                    artist.link = cursor.getString(5);
                    artist.description = cursor.getString(6);
                    artist.small = cursor.getString(7);
                    artist.big = cursor.getString(8);
                    artist.isFav = cursor.getInt(9) > 0;
                    artistsArrayList.add(artist);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return artistsArrayList;
    }

    public boolean isTableExists() {
        boolean result = false;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(CHECK_TABLE, null);
            result = cursor.getCount() > 0;
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}