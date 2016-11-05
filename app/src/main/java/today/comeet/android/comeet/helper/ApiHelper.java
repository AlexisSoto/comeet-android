package today.comeet.android.comeet.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sachavent on 20/10/2016.
 */
public class ApiHelper {

    private RequestQueue queue;
    private String url = "http://api.comeet.today:8080";

    public ApiHelper(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public void sendFbToken (final String token ) {
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, this.url+="/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("test", "reponse: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            } //Create an error listener to handle errors appropriately.

        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("fbToken", token); //Add the data you'd like to send to the server.
                return MyData;
            }
        };

        queue.add(MyStringRequest);
    }



}
