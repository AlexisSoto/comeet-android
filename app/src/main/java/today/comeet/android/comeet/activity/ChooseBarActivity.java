package today.comeet.android.comeet.activity;

import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import today.comeet.android.comeet.R;
import today.comeet.android.comeet.helper.GoogleApiHelper;

public class ChooseBarActivity extends AppCompatActivity {
    private final String TAG = "bar";
    private TextView txtgetbar;
    private JSONArray listebar;
    private int indextoShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pub);
        txtgetbar = (TextView) findViewById(R.id.getbar);
        indextoShow= 0;

    }
    @Override
    public void onResume() {
        super.onResume();

        // retrieve bar from google nearby shearch
        GoogleApiHelper googleapihelper = new GoogleApiHelper(this);
        // Get user geolocalisation
        LatLng latlng = new LatLng(48.737378, 2.423581);
        googleapihelper.retrieveNearbyPlaceData(latlng, 500, "bar", new GoogleApiHelper.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject resultat = new JSONObject(result);
                    Log.i("googleplace", "result json status: " + resultat.getString("status"));

                    // Recupère les réponses
                    listebar = resultat.getJSONArray("results");
                    Log.i("googleplace", "nombre de retour: " + listebar.length());

                    for (int i = 0; i < listebar.length(); i++) {
                        Log.d("test", "bar " + i + " name : " + listebar.getJSONObject(i).getString("name"));
                        Log.d("test", "bar " + i + " vicinity : " + listebar.getJSONObject(i).getString("vicinity"));
                        // Check if any photo is existing
                        if (!listebar.getJSONObject(i).isNull("photos")) {
                            Log.d("test", "bar " + i + " photo ref : " + listebar.getJSONObject(i).getJSONArray("photos").getJSONObject(0).getString("photo_reference"));
                        }
                    }

                    /* Load  retrieve data to Textview, imageview etc */
                    LoadContentbyIndex (indextoShow);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /* Load  retrieve data to Textview, imageview etc */
    private void LoadContentbyIndex (int index) {
        try {
            txtgetbar.setText("Nom: "+listebar.getJSONObject(index).getString("name")+"\nLocalisation: "+listebar.getJSONObject(index).getString("vicinity"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void btn_next_bar(View v) {
        // Check that indextoShow < listebar size
        if (indextoShow < listebar.length()) {
            indextoShow++;
        }
        LoadContentbyIndex (indextoShow);
    }

    public void btn_save_bar_and_event(View v) {
        // Get data from CreationEventActivity and put bar data too (localisation)
        Log.i ("tralala" , "boutton appuyé");
    }
}
