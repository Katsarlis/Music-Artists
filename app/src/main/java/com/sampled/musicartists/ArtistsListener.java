package com.sampled.musicartists;


import java.util.ArrayList;

public interface ArtistsListener {
    void addArtist(Artist artist);
    ArrayList<Artist> getArtists();
}
