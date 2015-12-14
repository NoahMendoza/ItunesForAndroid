package com.example.noah.itunesforandroid;

        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.media.Image;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.ViewGroup;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Button;

        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.InputStreamReader;
        import java.io.Reader;
        import java.io.StringWriter;
        import java.io.Writer;
        import java.net.URL;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import android.util.Log;
        import android.widget.Toast;

        import com.example.noah.itunesforandroid.movies.Movie;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.IOException;
        import java.io.InputStream;
        import java.io.BufferedReader;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ListView searchResults;
    private final String FILENAME = "MoviePrefs";

    private String attribute = "movieTerm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchResults =  (ListView)findViewById(R.id.search_resuls_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //final TextView test = (TextView)findViewById(R.id.test);
        final EditText searchBar = (EditText)findViewById(R.id.search_bar);
        final Button searchButton = (Button)findViewById(R.id.search_button);
        final Button directorField = (Button)findViewById(R.id.director_search);
        final Button actorField = (Button)findViewById(R.id.actor_search);
        final Button yearField = (Button)findViewById(R.id.year_search);
        final Button genreField = (Button)findViewById(R.id.genre_search);
        final Button movieField = (Button)findViewById(R.id.movie_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if(searchBar.getText().length() == 0)
                    searchBar.setError("That's a pretty vague search");

                else {

                    JSONAsyncTask requestThread = new JSONAsyncTask();
                    requestThread.execute(searchBar.getText().toString());

                    if(requestThread.getStatus() == AsyncTask.Status.FINISHED)
                    {
                        Context context = getApplicationContext();
                        CharSequence text = "Here ya go!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, text, duration).show();
                    }

                    //execute search
                }
            }
        });

        directorField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attribute = "directorTerm";
                directorField.setTextColor(Color.parseColor("#0000FF"));
                actorField.setTextColor(Color.parseColor("#FFFFFF"));
                genreField.setTextColor(Color.parseColor("#FFFFFF"));
                yearField.setTextColor(Color.parseColor("#FFFFFF"));
                movieField.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });


        actorField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attribute = "actorTerm";
                actorField.setTextColor(Color.parseColor("#0000FF"));
                directorField.setTextColor(Color.parseColor("#FFFFFF"));
                genreField.setTextColor(Color.parseColor("#FFFFFF"));
                yearField.setTextColor(Color.parseColor("#FFFFFF"));
                movieField.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        yearField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attribute = "releaseYearTerm";
                yearField.setTextColor(Color.parseColor("#0000FF"));
                actorField.setTextColor(Color.parseColor("#FFFFFF"));
                genreField.setTextColor(Color.parseColor("#FFFFFF"));
                directorField.setTextColor(Color.parseColor("#FFFFFF"));
                movieField.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        genreField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attribute = "genreTerm";
                genreField.setTextColor(Color.parseColor("#0000FF"));
                actorField.setTextColor(Color.parseColor("#FFFFFF"));
                directorField.setTextColor(Color.parseColor("#FFFFFF"));
                yearField.setTextColor(Color.parseColor("#FFFFFF"));
                movieField.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        movieField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attribute = "movieTerm";
                movieField.setTextColor(Color.parseColor("#0000FF"));
                actorField.setTextColor(Color.parseColor("#FFFFFF"));
                genreField.setTextColor(Color.parseColor("#FFFFFF"));
                yearField.setTextColor(Color.parseColor("#FFFFFF"));
                directorField.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void favoriteOnClick(View v) {
        ArrayList<String> titles = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = null, input="";
            while ((line = reader.readLine()) != null)
                    titles.add(line);
            reader.close();
            fis.close();

            for(String title : titles) {
                Log.d("LOG", title);
            }

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

    }


    public class JSONAsyncTask extends AsyncTask<String, String, List<Movie>>
    {
        private static final String TAG = "Homepage";
        //private List<Movie> movieList;

        @Override
        protected void onPostExecute(final List<Movie> result) {
            super.onPostExecute(result);

            Log.e("TEST", "Creating adapter");
            MovieAdapter adapter = new MovieAdapter(getApplicationContext(), R.layout.row, result);
            Log.e("TEST", "Adapter Created");

            Log.e("TEST", "Setting Adapter");

            ListView searchResults = (ListView)findViewById(R.id.search_resuls_view);
            searchResults.setAdapter(adapter);
            Log.e("TEST", "Adapter Set Successfully");
            searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                    i.putExtra("movieName", result.get(position).getMovieName());
                    i.putExtra("rating", result.get(position).getRating());
                    i.putExtra("director", result.get(position).getDirector());
                    i.putExtra("explicit", result.get(position).getExplicit());
                    i.putExtra("genre", result.get(position).getGenre());
                    i.putExtra("longDescription", result.get(position).getLongDescription());
                    i.putExtra("hdPrice", result.get(position).getHdPrice());
                    i.putExtra("rental", result.get(position).getRentalPrice());
                    i.putExtra("runTime", result.get(position).getRunTime());
                    i.putExtra("imageUrl", result.get(position).getImageUrl());
                    startActivity(i);

                }
            });
        }

        @Override
        protected List<Movie> doInBackground(String... urls) {

            String query = urls[0];
            query = query.replace(" ", "+");

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {

                Log.e("TEST", "Making connection");


               // URL url = new URL("https://itunes.apple.com/search?term=jack+johnson&limit=25");
                URL url = new URL("https://itunes.apple.com/search?term=" + query +
                        "&entity=movie&limit=25" + "&attribute=" + attribute);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Log.e("TEST", "Connected");

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null)
                {
                    buffer.append(line + "\n");
                }

                Log.d(TAG, buffer.toString());

                //Raw output from the API call
                String finalJSON = buffer.toString();

                //Get the actual array of movies
                JSONObject jsonParent = new JSONObject(finalJSON);
                JSONArray movieArray = jsonParent.getJSONArray("results");

                StringBuffer finalBufferData = new StringBuffer();

                List<Movie> movieList = new ArrayList<>();
                for(int i = 0; i < movieArray.length(); i++)
                {
                    JSONObject current = movieArray.getJSONObject(i);
                    String movieName = current.optString("trackName", "No Title");
                    String rating =  current.optString("contentAdvisoryRating", "No Parental Advisory");
                    String director = current.optString("artistName", "No Director Name");
                    String explicit = current.optString("trackExplicitness", "Unknown Explicitness");
                    String genre = current.optString("primaryGenreName", "No Genre");
                    String shortDescription = current.optString("shortDescription", "");
                    String longDescription = current.optString("longDescription", "");
                    String releaseDate = current.optString("releaseDate", "");
                    double hdPrice = current.optDouble("trackHdPrice", 0.0);
                    double regularPrice = current.optDouble("trackPrice", 0.0);
                    double rentalPrice = current.optDouble("trackRentalPrice", 0.0);
                    double hdRentalPrice = current.optDouble("trackHdRentalPrice", 0.0);
                    String runTime = current.optString("trackTimeMillis", "Runtime Unknown");
                    String imageUrl = current.optString("artworkUrl100", "none");

                    Movie currentMovie = new Movie(movieName,rating,director,explicit,genre,
                            shortDescription, longDescription, releaseDate, hdPrice, regularPrice,
                            rentalPrice, hdRentalPrice, runTime, imageUrl);

//                    finalBufferData.append(movieName + " " + rating + " " + director + " " +
//                            explicit + " " + genre + shortDescription + "" +
//                            longDescription + releaseDate + hdPrice + regularPrice
//                        + rentalPrice + hdRentalPrice + runTime);

                    movieList.add(currentMovie);
                }

                Log.e("TEST", "JSON Parsed");
                return movieList;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();
                try {
                    if(reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Return null if everything falls apart
            return null;
        }
    }


    public class MovieAdapter extends ArrayAdapter<Movie>{
        private List<Movie> movieList;
        private int resource;
        private LayoutInflater inflater;
        public MovieAdapter(Context context, int resource, List<Movie> objects) {

            super(context, resource, objects);
            Log.d("TEST", "Construct Adapter");
            movieList = objects;
            this.resource = resource;
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            Log.d("TEST", "Adapter Constructed");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView == null)
            {
                Log.e("TEST", "inflating");
                convertView = inflater.inflate(resource, null);
                Log.e("TEST", "inflated!");
            }

//
//            Movie currentMovie = new Movie(movieName,rating,director,explicit,genre,
//                    shortDescription, longDescription, releaseDate, hdPrice, regularPrice,
//                    rentalPrice, hdRentalPrice, runTime);

            ImageView movieIcon;
            TextView movieTitleView;
            TextView ratingView;
            TextView directorView;
            TextView runtimeView;
            TextView explicitView;
            TextView shortDescriptionView;
            TextView longDescriptionView;
            TextView releaseDateView;
            TextView hdPriceView;
            TextView regularPriceView;
            TextView rentalPriceView;
            TextView hdRentalPrice;

            // show The Image
            new DownloadImageTask((ImageView) convertView.findViewById(R.id.imageView))
                    .execute(movieList.get(position).getImageUrl());

            movieTitleView = (TextView)convertView.findViewById(R.id.title);
            directorView = (TextView)convertView.findViewById(R.id.director);
            releaseDateView = (TextView)convertView.findViewById(R.id.year);
            runtimeView = (TextView)convertView.findViewById(R.id.runningTime);
            shortDescriptionView = (TextView)convertView.findViewById(R.id.shortDescription);
            longDescriptionView = (TextView)convertView.findViewById(R.id.longDescription);

            movieTitleView.setText(movieList.get(position).getMovieName());
            directorView.setText(movieList.get(position).getDirector());
            releaseDateView.setText(movieList.get(position).getReleaseDate());
            runtimeView.setText(movieList.get(position).getRunTime());
            shortDescriptionView.setText(movieList.get(position).getShortDescription());
            longDescriptionView.setText(movieList.get(position).getLongDescription());

            return convertView;
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


}


