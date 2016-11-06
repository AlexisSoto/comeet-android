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
import today.comeet.android.comeet.helper.GoogleApiHelper;

public class ChooseBarActivity extends AppCompatActivity {
    private final String TAG = "bar";
    private TextView txtgetbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pub);
        txtgetbar = (TextView) findViewById(R.id.getbar);

        // retrieve bar from google nearby shearch
        GoogleApiHelper googleapihelper = new GoogleApiHelper(this);
        // Get user geolocalisation
        LatLng latlng = new LatLng( 48.737378, 2.423581);
        googleapihelper.retrieveNearbyPlaceData( latlng , 500, "bar");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
