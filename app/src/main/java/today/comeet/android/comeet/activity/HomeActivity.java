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
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.List;

import today.comeet.android.comeet.R;
import today.comeet.android.comeet.fragment.EventRecyclerViewFragment;
import today.comeet.android.comeet.fragment.FirstFragment;
import today.comeet.android.comeet.fragment.GoogleMapFragment;
import today.comeet.android.comeet.fragment.SecondFragment;
import today.comeet.android.comeet.fragment.ThirdFragment;
import com.roughike.bottombar.OnMenuTabClickListener;

public class HomeActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button btn_position;
    private double latitude;
    private double longitude;
    private String provider;
    private Context activityContext;
    FloatingActionButton btn_plus;
    FloatingActionButton bouttonplus;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;

    private BottomBar mBottomBar;
    private FragNavController fragNavController;

    //indices to fragments
    private final int TAB_FIRST = FragNavController.TAB1;
    private final int TAB_SECOND = FragNavController.TAB2;
    private final int TAB_THIRD = FragNavController.TAB3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loading home content layout
        setContentView(R.layout.activity_home);

        FragmentManager mFragmentManager;
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.container3, new EventRecyclerViewFragment());
        transaction.commit();

        // Ajout du fragment de Google Map
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container2, new GoogleMapFragment()).commit();


        // Récupère divers éléments du xml (à partir de leur id)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_position = (Button) findViewById(R.id.btn_position);

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

                /*txt_retourgps.append("\nlattitude :" + latitude);
                txt_retourgps.append("\nlongitude :" + longitude);

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                mMap.animateCamera(cameraUpdate);*/
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


        //FragNav
        //list of fragments
        List<Fragment> fragments = new ArrayList<>(3);

        //add fragments to list
        fragments.add(FirstFragment.newInstance(0));
        fragments.add(SecondFragment.newInstance(0));
        fragments.add(ThirdFragment.newInstance(0));

        //link fragments to container
        fragNavController = new FragNavController(getSupportFragmentManager(),R.id.container,fragments);
        //End of FragNav

        //BottomBar menu
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                //switch between tabs
                switch (menuItemId) {
                    case R.id.bottomBarItemOne:
                        fragNavController.switchTab(TAB_FIRST);
                        break;
                    case R.id.bottomBarItemSecond:
                        fragNavController.switchTab(TAB_SECOND);
                        break;
                    case R.id.bottomBarItemThird:
                        fragNavController.switchTab(TAB_THIRD);
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    fragNavController.clearStack();
                }
            }
        });
        //End of BottomBar menu

    }
    @Override
    public void onBackPressed() {
        if (fragNavController.getCurrentStack().size() > 1) {
            fragNavController.pop();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
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


   /* @Override
    public void onMapReady(GoogleMap googleMap) {
        // Demande de récupérer la position de l'utilisateur

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }*/

    public void btn_create_event  (View view) {
        Toast.makeText(getApplicationContext(), "Page creation evenement", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), CreationEventActivity.class));
    }

    // boutton de test pour lire des données dans la bdd SQLite
    public void btn_test_read_data  (View view) {

        Cursor cursor =
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

