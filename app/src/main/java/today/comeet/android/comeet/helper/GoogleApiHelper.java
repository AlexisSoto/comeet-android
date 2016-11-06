package today.comeet.android.comeet.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import today.comeet.android.comeet.R;

/**
 * Created by Annick on 06/11/2016.
 */

public class GoogleApiHelper {
    private RequestQueue queue;
    private String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    public GoogleApiHelper(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public void retrieveNearbyPlaceData (LatLng location, int radius, String types) {
        // Request a string response from the provided URL.
        Log.i("test","url :"+url+"location="+location.latitude+","+location.longitude+"&radius="+radius+"&types="+types+"&key=AIzaSyD7PnqYzH87nWyRlfdYR94O8nFLsq3Y-ik");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.url+="location="+location.latitude+","+location.longitude+"&radius="+radius+"&types="+types+"&key=AIzaSyD7PnqYzH87nWyRlfdYR94O8nFLsq3Y-ik",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject result = new JSONObject(response);
                            Log.d("test", "result json status: "+ result.getString("status"));
                            Log.d("test", "result json html_attributions: "+ result.getString("html_attributions"));

                            // Recupère les réponses
                            JSONArray listebar= result.getJSONArray("results");
                            Log.d("test", "taille du tableau json array: "+listebar.length());

                            for (int i=0; i < listebar.length(); i++) {
                                Log.d ("test", "bar "+i+" name : "+listebar.getJSONObject(i).getString("name"));
                                Log.d ("test", "bar "+i+" vicinity : "+listebar.getJSONObject(i).getString("vicinity"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
