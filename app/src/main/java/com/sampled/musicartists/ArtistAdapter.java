package com.sampled.musicartists;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


public class ArtistAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Artist> artistsList;
    ArrayList<Artist> results;



    public ArtistAdapter(Context context,ArrayList<Artist> artistsList){
        this.context=context;
        this.artistsList=artistsList;
        results =(ArrayList<Artist>) artistsList.clone();
    }


    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return results.get(position).id;
    }

    public void filter(String text) {
        Log.d("my","filtering "+text);
        text =text.toLowerCase();
        results.clear();
        if (text.length()==0)
            results.addAll(artistsList);
        else {
            Log.d("my", "filtering for " + text);
            for (Artist artist: artistsList ){
                if (artist.name.toLowerCase().contains(text)){
                    results.add(artist);
                }
            }
        }
        notifyDataSetChanged();
    }



    class ViewHolder{
        private ImageView imageView;
        private TextView nameTextView;
        private TextView genresTextView;
        private TextView albumsTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        Artist artist = results.get(position);
        if (convertView==null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
                    viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            viewHolder.genresTextView=(TextView)convertView.findViewById(R.id.genresTextView);
            viewHolder.albumsTextView = (TextView)convertView.findViewById(R.id.albumsTextView);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.imageView.setImageResource(R.drawable.music);
        viewHolder.nameTextView.setText(artist.name);
        String genres = "";
        for (int i=0;i<artist.genresArray.size();i++) {
            genres += artist.genresArray.get(i);
            if (i!=artist.genresArray.size()-1)
                genres+=", ";
        }
        viewHolder.genresTextView.setText("Genres: "+genres);
        viewHolder.albumsTextView.setText(artist.albums + " albums, " + artist.tracks + " tracks");
        Ion.with(viewHolder.imageView)
                .placeholder(R.drawable.music)
                .load(artist.small);


        return convertView;
    }



}