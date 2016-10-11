package today.comeet.android.comeet.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import today.comeet.android.comeet.R;


public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button btn_position;
    private double latitude;
    private double longitude;
    private String provider;
    private TextView txt_retourgps;
    private Context activityContext;
    private GoogleMap mMap;
    FloatingActionButton btn_plus;
    FloatingActionButton bouttonplus;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loading home content layout
        setContentView(R.layout.activity_home);


        // Récupère divers éléments du xml (à partir de leur id)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_position = (Button) findViewById(R.id.btn_position);
        txt_retourgps = (TextView) findViewById(R.id.text_retour);

        // Permet d'afficher la map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setSupportActionBar(toolbar);
        activityContext = this;

        // Création du menu en bas de la page
        /*bouttonplus = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        bouttonplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acces à la page creation d'un événement
                startActivity(new Intent(activityContext, CreationEventActivity.class));
            }
        });

        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);

        // Listener sur le premier bouton du menu
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                //Toast.makeText(getApplicationContext(), "Page creation evenement", Toast.LENGTH_LONG).show();
                // Acces à la page creation d'un événement
                startActivity(new Intent(activityContext, CreationEventActivity.class));


            }
        });*/

        // Lance le service de localisation
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creation d'un listerner pour update la localisation
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                //Stock la latitude et la longtitude
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                txt_retourgps.append("\nlattitude :" + latitude);
                txt_retourgps.append("\nlongitude :" + longitude);

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                mMap.animateCamera(cameraUpdate);
                if (ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.removeUpdates(locationListener);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
            }
            //return;
        } else {
            // Execute ce code s'il y a les permissions
            //btn_getPosition(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //btn_getPosition(this);

                }
                // return;
        }
    }

    public void btn_getPosition(final Context context) {

        // Quand on appuie sur le bouton
        btn_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // message test
                Toast.makeText(getApplicationContext(), "Position Enregistré", Toast.LENGTH_LONG).show();
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Paramètrage de la carte (avec les boutons zoom, localisation ...)
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        // Demande de récupérer la position de l'utilisateur

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

    public void btn_create_event  (View view) {
        Toast.makeText(getApplicationContext(), "Page creation evenement", Toast.LENGTH_LONG).show();
        startActivity(new Intent(activityContext, CreationEventActivity.class));
    }

    // boutton de test pour lire des données dans la bdd SQLite
    public void btn_test_read_data  (View view) {Cursor cursor =
            getContentResolver().query(Uri.parse("content://today.comeet.android.comeet/elements"), null, null,
                    null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            buffer.append("Id :"+ cursor.getString(0)+"\n");
            buffer.append("Name :"+ cursor.getString(1)+"\n");
            buffer.append("Description :"+ cursor.getString(2)+"\n");
            buffer.append("Localisation :"+ cursor.getString(3)+"\n\n");
            buffer.append("Date :"+ cursor.getString(4)+"\n\n");
            buffer.append("Lattitude :"+ cursor.getDouble(5)+"\n\n");
            buffer.append("Longitude :"+ cursor.getDouble(6)+"\n\n");
        }
        Log.d("test", "retour :" + buffer);
    }


}