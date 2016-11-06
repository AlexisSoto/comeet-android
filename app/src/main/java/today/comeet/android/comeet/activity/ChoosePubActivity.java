package today.comeet.android.comeet.activity;

import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import today.comeet.android.comeet.R;

public class ChoosePubActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = "bar";
    private GoogleApiClient googleApiClient;
    String query;
    LatLng nwLatLng;
    LatLng seLatLng;
    private TextView txtgetbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pub);
        txtgetbar = (TextView) findViewById(R.id.getbar);
        query = "bar";
        nwLatLng = new LatLng(49.837378,2.413581);
        seLatLng = new LatLng(48.727378,3.523581);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "connected");
        LatLngBounds latLngBounds = getScreenLatLngBounds(nwLatLng, seLatLng);
        Log.i (TAG, "latlngbounds "+latLngBounds);
        Log.i(TAG, "Center of LatLngBounds=" + latLngBounds.getCenter().latitude + ", " + latLngBounds.getCenter().longitude);
        PendingResult<AutocompletePredictionBuffer> pendingResult = getResults(query, latLngBounds);
        runRequest(pendingResult);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
    private PendingResult<AutocompletePredictionBuffer> getResults(String query, LatLngBounds latLngBounds) {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("FR")
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                .build();
                        Log.d("truc", "filter "+typeFilter);
        Log.d("truc", "latlng "+latLngBounds);
        return Places.GeoDataApi.getAutocompletePredictions(googleApiClient, query, latLngBounds, typeFilter);
    }

    private LatLngBounds getScreenLatLngBounds(LatLng northwest, LatLng southeast) {
        return new LatLngBounds.Builder().include(northwest).include(southeast).build();
    }

    private void awaitPendingResult(PendingResult pendingResult) {
        Log.i(TAG, "Getting results for \"" + query + "\"");
        AutocompletePredictionBuffer autocompletePredictions = (AutocompletePredictionBuffer)

                pendingResult.await(60, TimeUnit.SECONDS);
        for (AutocompletePrediction prediction : autocompletePredictions) {
            String place = "PlaceID=" + prediction.getPlaceId() + "\n";

            Places.GeoDataApi.getPlaceById(googleApiClient, prediction.getPlaceId())
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                final Place myPlace = places.get(0);
                                Log.i(TAG, "Place found: " + myPlace.getName());
                                txtgetbar.append("\n"+myPlace.getName());
                                Log.d("truc" , "okay: "+myPlace.getLatLng());
                            } else {
                                Log.e(TAG, "Place not found");
                            }
                            places.release();
                        }
                    });
            Log.i(TAG, place);
        }
        autocompletePredictions.release();
        Log.i(TAG, "Finished getting results");
    }

    private void runRequest(final PendingResult pendingResult) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                awaitPendingResult(pendingResult);
            }
        }).start();
    }
}
