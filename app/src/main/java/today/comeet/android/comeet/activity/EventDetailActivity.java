package today.comeet.android.comeet.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import today.comeet.android.comeet.R;
import today.comeet.android.comeet.listener.LocationListener;

public class EventDetailActivity extends AppCompatActivity {
    private int idEventToPrint;
    /* Attributs de l'événement */
    private String eventName;
    private String eventDescription;
    private String eventLocalisation;
    private String eventDate;

    /* Affichage des attributs */
    private TextView txtEventName;
    private TextView txtEventDescription;
    private TextView txtEventLocalisation;
    private TextView txtEventDate;
    private Button btnitineraire;
    private boolean permissionsEnabled;
    private LatLng latLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        // Check permissions
        ActivatePermissions();
        latLng = getPosition();

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            idEventToPrint = extras.getInt("id");
            // On incrémente l'id de 1 car il y a une différence de 1 entre la position dans le recyclerView et dans la bdd
            idEventToPrint++;
        }

        txtEventName = (TextView) findViewById(R.id.nameEvent);
        txtEventDate = (TextView) findViewById(R.id.eventDate);
        txtEventDescription = (TextView) findViewById(R.id.descriptionEvent);
        txtEventLocalisation = (TextView) findViewById(R.id.eventlocalisation);
        btnitineraire = (Button) findViewById(R.id.btn_itineraire);

        // Loading Choosen Event
        Cursor cursor = getContentResolver().query(Uri.parse("content://today.comeet.android.comeet/elements/" + idEventToPrint), null, null, null, null);

        while (cursor.moveToNext()) {
            eventName = cursor.getString(1);
            eventDescription = cursor.getString(2);
            eventLocalisation = cursor.getString(3);
            eventDate = cursor.getString(4);
        }
        // Printing in TextView
        txtEventName.append(eventName);
        txtEventDate.append(eventDate);
        txtEventDescription.append(eventDescription);
        if (eventLocalisation != null)
            txtEventLocalisation.append(eventLocalisation);
        else
            txtEventLocalisation.append("Non définit");
    }

    public void btnItineraireOnclick(View v) {
        Intent intent = new Intent(getApplicationContext(), ItineraireActivity.class);
        // send origin and destination adress
        intent.putExtra("origine", eventLocalisation.toString());
        if (latLng != null) {
            Log.d("bla", "lattitude: " + latLng.latitude);
            Log.d("bla", "longitude: " + latLng.longitude);
            startActivity(intent);
        }
    }

    public LatLng getPosition() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location location = null;
        if (network_enabled && locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
            Log.d("test ", "position activée");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }

            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                return latlng;

            } else
                Log.d("test ", "location null");
        } else {
            Log.d("test", "position non activé");
            locManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener(this, null), null);
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionsEnabled = true;
                } else {
                    Log.d("test ", "position refusée");
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setMessage("Activating position permissions is mandatory");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivatePermissions();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                }

        }
    }

    private void ActivatePermissions() {
        if (permissionsEnabled != true) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE
            }, 10);
        }
    }
}
