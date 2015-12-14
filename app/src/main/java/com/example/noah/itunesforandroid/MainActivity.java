package com.example.noah.itunesforandroid;

        import android.content.Context;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.AdapterView;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Button;
        import java.io.InputStreamReader;
        import java.io.Reader;
        import java.io.StringWriter;
        import java.io.Writer;
        import java.net.URL;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import android.util.Log;
        import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final TextView test = (TextView)findViewById(R.id.test);
        final EditText searchBar = (EditText)findViewById(R.id.search_bar);
        final Button searchButton = (Button)findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


    public class JSONAsyncTask extends AsyncTask<String, String, String>
    {
        private static final String TAG = "Homepage";

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            TextView test = (TextView)findViewById(R.id.test);
            test.setText(result);
        }

        @Override
        protected String doInBackground(String... urls) {
            String results = "";

            String query = urls[0];


            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {

               // URL url = new URL("https://itunes.apple.com/search?term=jack+johnson&limit=25");
                URL url = new URL("https://itunes.apple.com/search?term=jack&entity=movie&limit=25");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

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

                    finalBufferData.append(movieName + " " + rating + " " + director + " " +
                            explicit + " " + genre + shortDescription + "" +
                            longDescription + releaseDate + hdPrice + regularPrice
                        + rentalPrice + hdRentalPrice + runTime);
                }

                return finalBufferData.toString();
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


            Log.d(TAG, results);
            //Return null if everything falls apart
            return null;
        }
    }
}

