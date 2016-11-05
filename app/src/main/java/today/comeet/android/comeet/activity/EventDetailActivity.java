package today.comeet.android.comeet.activity;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import today.comeet.android.comeet.R;

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
            Log.d("test","onclick");
    }
}
