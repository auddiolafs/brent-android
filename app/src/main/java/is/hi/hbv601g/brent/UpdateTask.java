package is.hi.hbv601g.brent;

import android.os.AsyncTask;

import com.google.api.Http;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateTask extends AsyncTask<JSONObject, Integer, JSONObject> {

    UpdateTaskCallback listener;
    private String baseUrl = "https://hugbo2.herokuapp.com";

    public UpdateTask(UpdateTaskCallback listener) {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... args) {

        HttpURLConnection urlConnection = null;
        JSONObject response = null;
        try {
            String endpoint = (String) args[0].remove("endpoint");
            String method = (String) args[0].remove("method");
            JSONObject body = args[0];
            URL url = new URL(baseUrl + endpoint);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod(method);
            String bodyString = body.toString();
            byte[] outputInBytes = bodyString.getBytes();
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(outputInBytes);
            outputStream.close();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close(); // close out the input stream
                response = new JSONObject(stringBuilder.toString());

            } else {
                response = new JSONObject();
                response.put("error", "Server responded with response code " + responseCode);
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        catch (JSONException je) {
            je.printStackTrace();
        }

        finally {
            urlConnection.disconnect();
        }
        return response;
    }

    protected void onPostExecute(JSONObject result) {
        listener.onResultReceived(result);
    }

    public interface UpdateTaskCallback {
        void onResultReceived(JSONObject result);
    }

}


