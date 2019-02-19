package is.hi.hbv601g.brent;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpService implements UpdateTask.UpdateTaskCallback {

    HttpServiceCallback listener;

    public HttpService(HttpServiceCallback listener) {
        this.listener = listener;
    }

    public void post(String endpoint, JSONObject body) {
        try {
            body.put("endpoint", parseEndpoint(endpoint));
            body.put("method", "POST");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new UpdateTask(this).execute(body);
    }

    public void delete(String endpoint, JSONObject body) {
        JSONObject a = new JSONObject();
        try {
            body.put("endpoint", parseEndpoint(endpoint));
            body.put("method", "DELETE");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new UpdateTask(this).execute(a);
    }

    public void put(String endpoint, JSONObject body) {
        JSONObject a = new JSONObject();
        try {
            body.put("endpoint", parseEndpoint(endpoint));
            body.put("method", "PUT");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new UpdateTask(this).execute(a);
    }

    private String parseEndpoint(String endpoint) {
        if (endpoint.substring(0,1) != "/") {
            endpoint = "/" + endpoint;
        }
        return endpoint;
    }

    @Override
    public void onResultReceived(JSONObject result) {
        listener.onResultReceived(result);
    }

    public interface HttpServiceCallback {
        void onResultReceived(JSONObject result);
    }
}
