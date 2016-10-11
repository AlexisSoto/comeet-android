package today.comeet.android.comeet.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import today.comeet.android.comeet.helper.DBHelper;
import today.comeet.android.comeet.R;

public class MainActivity extends AppCompatActivity {
    DBHelper database;
    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PACKAGE_NAME = getApplicationContext().getPackageName();

        setContentView(R.layout.activity_main);
        database= new DBHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Button redirect to homeActivity
    public void btn_GotoHome(View view) {

        // message test
        //Toast.makeText(getApplicationContext(), "Vous etes log", Toast.LENGTH_LONG).show();

        //Go to Home Page
        Intent intend= new Intent(this,HomeActivity.class);
        startActivity(intend);
    }

    // boutton de test pour lire des données dans la bdd SQLite
    public void btn_test_read_data  (View view) {
       Cursor retour  = database.getAllData();
        Log.d("lecture ", "test1 :"+retour);

        StringBuffer buffer = new StringBuffer();
        while (retour.moveToNext()) {
            buffer.append("Id :"+ retour.getString(0)+"\n");
            buffer.append("Name :"+ retour.getString(1)+"\n");
            buffer.append("Description :"+ retour.getString(2)+"\n");
            buffer.append("Localisation :"+ retour.getString(3)+"\n\n");
            buffer.append("Date :"+ retour.getString(4)+"\n\n");
            buffer.append("Lattitude :"+ retour.getString(5)+"\n\n");
            buffer.append("Longitude :"+ retour.getString(6)+"\n\n");
        }
        Log.d("lecture ", "retour :"+buffer);
    }
}
