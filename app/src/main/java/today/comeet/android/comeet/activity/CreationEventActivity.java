package today.comeet.android.comeet.activity;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONException;
import org.json.JSONObject;

import today.comeet.android.comeet.helper.DBHelper;
import today.comeet.android.comeet.R;
import today.comeet.android.comeet.provider.EventContentProvider;


public class CreationEventActivity extends AppCompatActivity {

    private DatePickerDialog dpd = null;
    private TimePickerDialog timepicker;
    private EditText eventName;
    private EditText eventDescription;
    private String date;
    private String heure;
    private Place place;
    private Button friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_event);
        eventName = (EditText) findViewById(R.id.event_name);
        eventDescription = (EditText) findViewById(R.id.event_description);
        friends = (Button) findViewById(R.id.add_friends);
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
        /*String dateEtHeure = "";
        if (date != null)
            dateEtHeure += date;
        if (heure != null)
            dateEtHeure += " " + heure;

        if (heure == null && date == null)
            dateEtHeure = "Non définit";

        // Ajout dans la base de données
        ContentValues contentValues = new ContentValues();

        if (!eventName.getText().toString().isEmpty())
            contentValues.put(DBHelper.COL_2, eventName.getText().toString());
        else
            contentValues.put(DBHelper.COL_2,"Non définit");

        if (!eventDescription.getText().toString().isEmpty())
            contentValues.put(DBHelper.COL_3, eventDescription.getText().toString());
        else
            contentValues.put(DBHelper.COL_3,"Non définit");

        contentValues.put(DBHelper.COL_5, dateEtHeure);
        if (place != null) {
            contentValues.put(DBHelper.COL_4, place.getAddress().toString());
            contentValues.put(DBHelper.COL_6, place.getLatLng().latitude);
            contentValues.put(DBHelper.COL_7, place.getLatLng().longitude);
        }
        Uri result = getContentResolver().insert(EventContentProvider.CONTENT_URL, contentValues);
        notification();*/

        // Loading ChoosePubActivity
        Intent intent = new Intent(getApplicationContext(), ChoosePubActivity.class);
        startActivity(intent);
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

    // Fonction qui permet d'effectuer une notification (qui vibre)
    private void notification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Comeet")
                .setContentText("Création événement");

        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, builder.build());

    }

    public void btn_friends (View v) {
        Log.d("friend", "boutton click");
        // we will query the name with the openGraph API
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // this code is executed when the response from the API is received (async)
                        try {
                            Log.d("friend", "name: "+  object.getString("friends"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        // we pass the right parameters for the query : fields=name
        Bundle parameters = new Bundle();
        parameters.putString("fields", "friends");
        request.setParameters(parameters);
        request.executeAsync(); // execute the query
    }
}
