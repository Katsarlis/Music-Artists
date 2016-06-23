package com.sampled.musicartists;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.SearchEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ProgressBar progressBar;
    ArtistAdapter adapter;
    ArrayList<Artist> artistsList;
    DBHelper helper;


    public static String LOG_TAG = "my_log";

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        listView = (ListView) findViewById(R.id.listView);
        helper = new DBHelper(this);
        progressBar.setVisibility(View.VISIBLE);

        if (!helper.isTableExists()&&isOnline()) {
            new ParseData().execute();
        } else {
            ArrayList<Artist> artists = helper.getArtists();
            adapter = new ArtistAdapter(MainActivity.this, artists);
            progressBar.setVisibility(View.GONE);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(LOG_TAG,"Searching "+newText);
                adapter.filter(newText);
                return true;
            }
        });

        return true;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, DetailsActivity.class);
        Artist artist = (Artist)parent.getItemAtPosition(position);
        Log.d(LOG_TAG,artist.name);
        intent.putExtra("name", artist.name);
        intent.putExtra("albums",artist.albums+" albums, "+artist.tracks+" tracks");
        String genres = "";
        for (int i=0;i<artist.genresArray.size();i++) {
            genres += artist.genresArray.get(i);
            if (i!=artist.genresArray.size()-1)
                genres+=", ";
        }
        intent.putExtra("genres",genres);
        intent.putExtra("description",artist.description);
        intent.putExtra("big",artist.big);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }

    public ArrayList<Artist> getArtistsList(){
        return artistsList;
    }


    private class ParseData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultJson = "";


            try {
                URL url = new URL("http://cache-spb09.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                artistsList = new ArrayList<Artist>();


                JSONArray dataJsonArray = new JSONArray(resultJson);


                for (int i = 0; i < dataJsonArray.length(); i++) {
                    Artist artist = new Artist();
                    JSONObject artistData = dataJsonArray.getJSONObject(i);
                    artist.id = artistData.getInt("id");
                    artist.name = artistData.getString("name");
                    JSONArray genres = artistData.getJSONArray("genres");
                    artist.genres = artistData.getString("genres");
                    Log.d(LOG_TAG,artist.genres);
                    artist.genresArray = new ArrayList<>();
                    for (int j = 0;j<genres.length();j++){
                        artist.genresArray.add(genres.getString(j));
                    }
                    artist.tracks = artistData.getInt("tracks");
                    artist.albums = artistData.getInt("albums");
                    artist.description = artistData.getString("description");
                    JSONObject cover = artistData.getJSONObject("cover");
                    artist.small = cover.getString("small");
                    artist.big = cover.getString("big");

                    helper.addArtist(artist);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            ArrayList<Artist> artistsList = helper.getArtists();
            adapter = new ArtistAdapter(MainActivity.this, artistsList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(MainActivity.this);
            progressBar.setVisibility(View.GONE);
        }
    }





}
