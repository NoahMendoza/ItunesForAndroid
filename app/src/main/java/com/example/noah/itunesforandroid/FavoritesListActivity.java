package com.example.noah.itunesforandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FavoritesListActivity extends AppCompatActivity {
    private final String FILENAME = "MoviePrefs";


    public class favItem {
        String title;
        String imageUrl;
        String description;
    }

    public class favItemAdapter extends ArrayAdapter<favItem> {

        public favItemAdapter(Context context, ArrayList<favItem> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            favItem result = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fav_list_item, parent, false);
            }

            // show The Image
            new DownloadImageTask((ImageView) convertView.findViewById(R.id.movie_image))
                    .execute(result.imageUrl);

            ((TextView)convertView.findViewById(R.id.movie_title)).setText(result.title);
            ((TextView)convertView.findViewById(R.id.movie_desc)).setText(result.description);

            return convertView;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);

        ListView favListView = (ListView)findViewById(R.id.fav_list_view);
        ArrayList<favItem> favoriteItems = new ArrayList<>();
        favItemAdapter adapter = new favItemAdapter(this, favoriteItems);
        favListView.setAdapter(adapter);
        adapter.clear();

        populate(favoriteItems);
        //Comment that will hopefully help github stuff

    }

    private void populate(ArrayList<favItem> items) {
        ArrayList<String> titles = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = null;

            while ((line = reader.readLine()) != null) {
                titles.add(line); //This just gets all data from the storage
            }

            //Because I know exactly how the storage is set up, I can do this
            for(int i = 0; i < titles.size(); i+= 3) {
                favItem a = new favItem();
                a.title = titles.get(i);
                a.description = titles.get(i + 1);
                a.imageUrl = titles.get(i + 2);
                items.add(a);
            }
            reader.close();
            fis.close();
            for(String title : titles) {
                Log.d("LOG", title);
            }

        } catch (FileNotFoundException e) {

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
           // Bitmap resized = Bitmap.createScaledBitmap(result, 309, 224, true);
            bmImage.setImageBitmap(result);
        }
    }

}
