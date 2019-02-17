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
import java.util.List;

public class FetchTask extends AsyncTask<String, Integer, List<JSONObject>> {

    FetchTaskCallback listener;

    public FetchTask(FetchTaskCallback listener) {
        this.listener = listener;
    }

    @Override
    protected List<JSONObject> doInBackground(String... routes) {

        InputStream in = null;
        HttpURLConnection urlConnection = null;
        JSONObject jsonResponse = null;
        List<JSONObject> responses = new ArrayList<>();
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
                        // This could possibly be an JSON object instead of JSON array
                        // (dependant on the response)
                        jsonResponse = new JSONObject(stringBuilder.toString());
                        responses.add(jsonResponse);

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

        protected void onPostExecute(List<JSONObject> result) {
        listener.onResultReceived(result);
    }

    public interface FetchTaskCallback {
        void onResultReceived(List <JSONObject> result);
    }

}
