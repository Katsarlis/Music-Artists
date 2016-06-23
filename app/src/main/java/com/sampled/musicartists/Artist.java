package com.sampled.musicartists;



import java.util.ArrayList;

public class Artist {
    int id;
    String name;
    String genres;
    ArrayList<String> genresArray;
    int tracks;
    int albums;
    String link;
    String description;
    String small;
    String big;
    boolean isFav=false;


    public Artist(){
    }

    public Artist(
            int id, String name,
            String genres,
            int tracks,
            int albums,
            String link,
            String description,
            String small,
            String big) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.tracks = tracks;
        this.albums = albums;
        this.link = link;
        this.description = description;
        this.small = small;
        this.big = big;
    }



}
