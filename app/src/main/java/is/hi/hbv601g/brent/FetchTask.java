package is.hi.hbv601g.brent;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FetchTask extends AsyncTask<String, Integer, Map<String,JSONArray>> {

    FetchTaskCallback listener;

    public FetchTask(FetchTaskCallback listener) {
        this.listener = listener;
    }

    @Override
    protected Map<String,JSONArray> doInBackground(String... routes) {

        InputStream in = null;
        HttpURLConnection urlConnection = null;
        JSONObject response = null;
        Map<String,JSONArray> responses = new HashMap<>();
        for (int i = 0; i<routes.length; i+=1) {
            String route = routes[i];
            try {
                // Replace URL!
                URL url = new URL("https://hugbo2.herokuapp.com" + route);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");

                StringBuilder stringBuilder = new StringBuilder();
                int responseCode = urlConnection.getResponseCode();

                // Check to make sure we got a valid status response from the server,
                // then get the server JSON response if we did.
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    //read in each line of the response to the input buffer
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    bufferedReader.close(); // close out the input stream

                    try {
                        response = new JSONObject(stringBuilder.toString());
                        // Because we are possibly handling responses from multiple endpoints
                        // It is necessary to store them as key val pairs
                        Iterator<String> keys = response.keys();
                        while(keys.hasNext()) {
                            String key = keys.next();
                            JSONArray val = null;
                            if (response.get(key) instanceof JSONObject) {
                                JSONObject obj = (JSONObject) response.get(key);
                                val = new JSONArray();
                                val.put(obj);
                            } else if (response.get(key) instanceof JSONArray){
                                val = (JSONArray) response.get(key);
                            }
                            responses.put(key, val);
                        }

                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                }
            } catch (IOException e) {

            } finally {
                urlConnection.disconnect();
            }

        }
        
        return responses;
    }

        protected void onPostExecute(Map<String,JSONArray> result) {
        listener.onResultReceived(result);
    }

    public interface FetchTaskCallback {
        void onResultReceived(Map <String,JSONArray> result);
    }

}
