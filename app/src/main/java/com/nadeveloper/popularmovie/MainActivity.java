package com.nadeveloper.popularmovie;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    //title, release date, movie poster, vote average, and plot synopsis(overview).
    private final String TITLE = "title";
    private final String PLOT_SYNOPSIS = "overview";
    private final String MOVIE_POSTER = "poster_path";
    private final String RELEASE_DATE = "release_date";
    private final String VOTE_AVERAGE = "vote_average";

    //store JSON response from API
    private String resultString = null;
    private JSONArray movieDetailsJSON;
    //single page returns 20 result
    private String[] imgUrl = new String[20];
    private GridView gridView;

    // current_choice of category
    String current_choice = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Handle Grid View
        handleGridView(current_choice);
    }

    private void handleGridView(String choice) {
        current_choice = choice;

        if (FetchMovie()){
            gridView =  findViewById(R.id.gridView);
            gridView.setAdapter(new ImageAdapter(this, imgUrl));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try{
                        //invoke new activity on click to display details
                        //get details from data to pass in Intent
                        JSONObject object = movieDetailsJSON.getJSONObject(position);
                        String title = object.getString(TITLE);
                        String poster = object.getString(MOVIE_POSTER);
                        String release_date = object.getString(RELEASE_DATE);
                        String vote = object.getString(VOTE_AVERAGE);
                        String plot = object.getString(PLOT_SYNOPSIS);

                        //invoke new activity with Intent
                        Intent intent = new Intent(getApplicationContext(), MovieDetails.class);
                        intent.putExtra(TITLE,title);
                        intent.putExtra(MOVIE_POSTER, poster);
                        intent.putExtra(RELEASE_DATE,release_date);
                        intent.putExtra(VOTE_AVERAGE,vote);
                        intent.putExtra(PLOT_SYNOPSIS,plot);

                        startActivity(intent);

                    }catch (JSONException e){
                        e.printStackTrace();
                        showErrorDialog();
                    }
                }
            });
        } else {
            //we can't get data so show error dialog
            showErrorDialog();
        }
    }

    //Inflate Menu and Handle Click Events
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.most_popular:
                current_choice = "popular";
                handleGridView(current_choice);
                return true;
            case R.id.most_rated:
                current_choice = "top_rated";
                handleGridView(current_choice);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setCancelable(true)
                .setMessage("Something is Wrong !")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private boolean FetchMovie() {
        DownloadTask downloadData = new DownloadTask();
        try {
            resultString = downloadData.execute(current_choice).get();
            if (resultString != null) {
                JSONObject movie = new JSONObject(resultString);
                movieDetailsJSON = movie.getJSONArray("results");
                for (int i = 0; i < movieDetailsJSON.length(); i++) {
                    JSONObject temp_mov = movieDetailsJSON.getJSONObject(i);
                    imgUrl[i] = "http://image.tmdb.org/t/p/w185/" + temp_mov.getString("poster_path");
                }
                return true;
            } else
                return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}