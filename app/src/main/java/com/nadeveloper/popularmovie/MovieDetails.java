package com.nadeveloper.popularmovie;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetails extends AppCompatActivity {

    private final String TITLE = "title";
    private final String PLOT_SYNOPSIS = "overview";
    private final String MOVIE_POSTER = "poster_path";
    private final String RELEASE_DATE = "release_date";
    private final String VOTE_AVERAGE = "vote_average";
    String title;
    String release;
    String poster;
    String vote;
    String plot;
    private TextView plotView, voteAvg, releaseDate, Title;
    private ImageView posterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //fill stuff in UI
        plotView =  findViewById(R.id.plotView);
        voteAvg =  findViewById(R.id.voteAvg);
        releaseDate =  findViewById(R.id.releaseDate);
        Title =  findViewById(R.id.mainTitle);
        posterImage =  findViewById(R.id.poster);

        if (uiUpdate()) {
            Log.d("success", "success in updating UI");
        } else
            showErrorDialog();
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(MovieDetails.this)
                .setCancelable(true)
                //trying to being funny in Error Message
                .setMessage("Everyone Okay, Because This thing is Broken!")
                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    private boolean uiUpdate() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(TITLE) && extras.containsKey(RELEASE_DATE) && extras.containsKey(MOVIE_POSTER) && extras.containsKey(VOTE_AVERAGE) && extras.containsKey(PLOT_SYNOPSIS)) {
                title = intent.getStringExtra(TITLE);
                release = intent.getStringExtra(RELEASE_DATE);
                poster = intent.getStringExtra(MOVIE_POSTER);
                vote = intent.getStringExtra(VOTE_AVERAGE);
                plot = intent.getStringExtra(PLOT_SYNOPSIS);
                poster = "http://image.tmdb.org/t/p/w500/" + poster;

            } else
                return false;
        } else
            return false;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(release);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        releaseDate.setText("Release Date : " + df.format(date).toString());
        Title.setText("" + title);
        voteAvg.setText("Vote Average : " + vote.toString());
        plotView.setText("" + plot);
        Picasso.with(this).load(poster).into(posterImage);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}