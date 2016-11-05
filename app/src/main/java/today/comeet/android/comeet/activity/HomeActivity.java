package today.comeet.android.comeet.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.List;

import today.comeet.android.comeet.R;
import today.comeet.android.comeet.fragment.FbLoginFragment;
import today.comeet.android.comeet.fragment.FirstFragment;
import today.comeet.android.comeet.fragment.LoginFragment;
import today.comeet.android.comeet.fragment.SecondFragment;
import today.comeet.android.comeet.fragment.ProfileFragment;
import com.roughike.bottombar.OnMenuTabClickListener;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private boolean permissionsEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check permissions
        ActivatePermissions();

        // Loading first fragment
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(android.R.id.content, new FirstFragment());
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void btn_create_event  (View view) {
        Toast.makeText(getApplicationContext(), "Page creation evenement", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), CreationEventActivity.class));
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

    // This method is called when we click on the button to add a friend as favorite
    public void addFavoriteFriend(View view) {
        Intent intent = new Intent(this, AddFriendActivity.class);
        startActivity(intent);
    }
}

