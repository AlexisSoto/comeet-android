package today.comeet.android.comeet.activity;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import today.comeet.android.comeet.helper.DBHelper;
import today.comeet.android.comeet.R;


public class CreationEventActivity extends AppCompatActivity {

    DatePickerDialog dpd = null;
    TimePickerDialog timepicker;
    EditText eventName;
    EditText eventDescription;
    String date;
    String heure;
    Place place;
    DBHelper database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_event);
        eventName = (EditText) findViewById(R.id.event_name);
        eventDescription = (EditText) findViewById(R.id.event_description);
        // Initialisation de la database
        database= new DBHelper(this);
    }

    // Boutton pour choisir la date
    public void btn_ChooseDate(View v) {
        dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // txt_confirmation_date.setText("Date:" + dayOfMonth + " " + (monthOfYear + 1) + " " + year);
                date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            }
        }, 2015, 11, 26);
        dpd.show();
    }

    // Boutton pour choisir l'heure
    public void btn_Choosehour(View v) {
        timepicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Toast.makeText(getApplicationContext(), "Heure choisit", Toast.LENGTH_LONG).show();
                heure = hourOfDay + ":" + minute + ":00";
            }
        }, 7, 40, true);
        timepicker.show();

    }

    // Boutton pour créer un nouvel événement
    public void btn_new_event(View v) {
        // Il faut récupérer les différents éléments (date,heures etc)
        Log.d("Creation", "event_name recup: " + eventName.getText().toString());
        Log.d("Creation", "event_description recup: " + eventDescription.getText().toString());
        Log.d("Creation", "date recup: " + date);
        Log.d("Creation", "heure recup: " + heure);
        // Il faut s'assurer que place != null --> sinon erreur
        if (place != null) {
            Log.d("Creation", "Place: " + place.getAddress() + place.getPhoneNumber());
        }
        else {
            Log.d("Creation", "place null");
        }
        String dateEtHeure = date+" "+heure;
        Log.d("Creation", "date et heure :"+dateEtHeure);
        boolean isInserted = database.insertData(eventName.getText().toString(),
                eventDescription.getText().toString(),
                place.getAddress().toString(), dateEtHeure,place.getLatLng().latitude, place.getLatLng().longitude );
        Log.d("Creation ", "is insertion okay? :"+isInserted);

        notification();

    }

    // Boutton pour choisir la localisation de l'événement
    public void autocompletePlace(View view) {
        try {
            // Ouvre une nouvelle fenêtre pour ajouter la localisation souhaité
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            // Indique qu'on attends un résultat (dans notre cas, une adresse)
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    // Fonction qui s'exécute quand on a choisit une adresse
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Si tout s'est bien déroulé
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Récupère les données de la place choisit
                place = PlaceAutocomplete.getPlace(this, data);
            }
            // Dans le cas où il y a une erreur
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());
            }
            // Si l'utilisateur retourne en arrière
            else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    // Fonction pour faire une notification
    private void notification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Comeet")
                .setContentText("Création événement");

        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, builder.build());

    }
}
