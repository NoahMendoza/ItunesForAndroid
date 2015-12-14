package com.example.noah.itunesforandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DetailsActivity extends AppCompatActivity {

    private String movie;
    private String description;
    private String imageUrl;
    private final String FILENAME = "MoviePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent i = getIntent();

        // show The Image
        new DownloadImageTask(((ImageView)findViewById(R.id.image_view)))
                .execute(i.getStringExtra("imageUrl"));
        imageUrl = i.getStringExtra("imageUrl") + "\n";


        ((TextView)findViewById(R.id.movie_name)).setText("Title: " + i.getStringExtra("movieName"));
        movie = i.getStringExtra("movieName") + "\n";
        ((TextView)findViewById(R.id.rating)).setText("Rating: " + i.getStringExtra("rating"));
        ((TextView)findViewById(R.id.director)).setText("Director: " + i.getStringExtra("director"));
        ((TextView)findViewById(R.id.genre)).setText("Genre: " + i.getStringExtra("genre"));
        ((TextView)findViewById(R.id.description)).setText("Description: " + i.getStringExtra("longDescription"));
        description = i.getStringExtra("longDescription") + "\n";
        ((TextView)findViewById(R.id.explicit)).setText("explicit: " + i.getStringExtra("explicit"));
        ((TextView)findViewById(R.id.runTime)).setText("Run Time: " + i.getStringExtra("runTime"));
        ((TextView)findViewById(R.id.hdPrice)).setText("HD Move Price: $" + Double.toString(i.getDoubleExtra("hdPrice", 0.0)));
        ((TextView)findViewById(R.id.rental)).setText("Rental Move Price: $" + Double.toString(i.getDoubleExtra("rental", 0.0)));

    }

    public void favOnClick(View v) {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
            fos.flush();
            fos.write(movie.getBytes());
            fos.write(description.getBytes());
            fos.write(imageUrl.getBytes());
            fos.close();
        } catch(FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            //Bitmap resized = Bitmap.createScaledBitmap(result, 309, 224, true);
            bmImage.setImageBitmap(result);
        }
    }

}
