package today.comeet.android.comeet.activity;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import today.comeet.android.comeet.R;

public class EventDetail extends AppCompatActivity {
    private int idEventToPrint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            idEventToPrint = extras.getInt("id");
            // On incrémente l'id de 1 car il y a une différence de 1 entre la position dans le recyclerView et dans la bdd
            idEventToPrint++;
        }

        // Loading Choosen Event
        Cursor cursor =
                getContentResolver().query(Uri.parse("content://today.comeet.android.comeet/elements/"+idEventToPrint), null, null,
                        null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            buffer.append("Id :"+ cursor.getString(0)+"\n");
            buffer.append("Evenement :"+ cursor.getString(1)+"\n");
            buffer.append("Description :"+ cursor.getString(2)+"\n");
            buffer.append("Localisation :"+ cursor.getString(3)+"\n\n");
            buffer.append("Le :"+ cursor.getString(4));
            buffer.append("Lattitude :"+ cursor.getDouble(5)+"\n\n");
            buffer.append("Longitude :"+ cursor.getDouble(6)+"\n\n");
        }
        Log.d("retour", "retour: "+buffer);
    }
}
