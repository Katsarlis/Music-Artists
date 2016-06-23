package com.sampled.musicartists;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

public class DetailsActivity extends AppCompatActivity {

    ImageView imageView;
    TextView albums;
    TextView genres;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        setTitle(getIntent().getExtras().getString("name"));
        imageView = (ImageView)findViewById(R.id.image);
        //imageView.setImageResource(R.drawable.music);
        albums = (TextView)findViewById(R.id.albumsTextView);
        albums.setText(getIntent().getExtras().getString("albums"));
        genres = (TextView)findViewById(R.id.genresTextView);
        genres.setText("Genres: "+getIntent().getExtras().getString("genres"));
        description = (TextView)findViewById(R.id.description);
        description.setText("Description: " + getIntent().getExtras().getString("description"));
        Ion.with(imageView)
                .placeholder(R.drawable.music)
                .load(getIntent().getExtras().getString("big"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_left);
    }
}
