package is.hi.hbv601g.brent;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchTask extends AsyncTask<String, Integer, JSONArray> {

    FetchTaskCallback listener;

    public FetchTask(FetchTaskCallback listener) {
        this.listener = listener;
    }

    @Override
    protected JSONArray doInBackground(String... path) {

        InputStream in = null;
        HttpURLConnection urlConnection = null;
        JSONArray jsonResponse = null;
        try {
            URL url = new URL("https://req-prototype.herokuapp.com" + path[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");

            StringBuilder stringBuilder = new StringBuilder();
            int responseCode = urlConnection.getResponseCode();

            //Check to make sure we got a valid status response from the server,
            //then get the server JSON response if we did.
            if (responseCode == HttpURLConnection.HTTP_OK) {

                //read in each line of the response to the input buffer
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close(); //close out the input stream

                try {
                    //Copy the JSON response to a local JSONObject
                    jsonResponse = new JSONArray(stringBuilder.toString());
                    System.out.println(jsonResponse);
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        } catch (IOException e) {

        } finally {
            urlConnection.disconnect();
        }

        return jsonResponse;
    }

        protected void onPostExecute(JSONArray result) {
        listener.onResultReceived(result);
    }

    public interface FetchTaskCallback {
        void onResultReceived(JSONArray result);
    }

}
