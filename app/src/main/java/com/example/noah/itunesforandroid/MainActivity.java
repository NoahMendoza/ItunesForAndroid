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

    class JSONAsyncTask extends AsyncTask<String, Void, String>
    {
        private static final String TAG = "Homepage";

        @Override
        protected String doInBackground(String... urls) {

            String results = "";
            Log.d(TAG, results + "testing!");
            Log.d(TAG, "WHAAAAAAAAAAAAAAAAAAT");
            for(String query:urls){
                results = makeNetworkRequest(query);
            }

            Log.d(TAG, results);
            return results;
        }


        //make a network request
        public String makeNetworkRequest(String query)
        {
            String response = "";
            Log.d(TAG, response);
            try {
                //Set the query string
                query = query.replace(" ","&");
                String urlString = "https://itunes.apple.com/search?term=jack+johnson";
                Log.d(TAG, urlString);

                //Create the request
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                //Connect and make sure it worked
                connection.connect();
                int responseCode = connection.getResponseCode();
                Log.d(TAG, "The response code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK)
                {

                    Log.d("TAG", "getting input stream");
                    response = convertToString(connection.getInputStream());
                    Log.d(TAG, response);
                    Log.d(TAG, "input assigned");
                }
                else
                {
                    Log.e(TAG, "Uh oh, our URL might not be right!");

                }

                connection.disconnect();

            }
            //Exception handling
            catch (MalformedURLException e)
            {
                Log.e(TAG, "Your URL is malformed!", e);
            }
            catch (IOException e)
            {
                Log.e(TAG, "Could not connect to the URL", e);
            }

            return response;
        }

        //Convert JSON returned from get request into a string
        public String convertToString(InputStream inputStream) throws IOException
        {
            Log.d(TAG, "converting to string");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            Log.d(TAG, reader.readLine());
            while ((line = reader.readLine()) != null)
            {
                Log.d(TAG, "line" + line);
                builder.append(line+"\n");
                Log.d(TAG, builder.toString());
            }
            reader.close();
            Log.d(TAG,"Conversion done");
            Log.d(TAG,builder.toString() + "THE RESPONSE STRING");
            Log.d(TAG,"I AM SO CONFUSED");

            int results = builder.length();

            Log.d(TAG, "fucking shit");
            //Log.d(TAG, results);

            return builder.toString();


        }


    }
}
