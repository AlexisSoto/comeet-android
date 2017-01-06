package today.comeet.android.comeet.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import today.comeet.android.comeet.R;

public class ChooseFriendsActivity extends AppCompatActivity {

    private ListView friendsListView;
    private ArrayList<String> friendsListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friends);

        /* Get Facebook friends list*/
        if (null != getIntent().getStringArrayListExtra("friends_list")) {
            friendsListName = getIntent().getStringArrayListExtra("friends_list");
            Log.i("friend", "friend list recupéré dans l'activity: "+friendsListName);
        } else {
            Log.e ("friends_activity", "problème récupération liste d'ami");
        }

        friendsListView = (ListView) findViewById(R.id.listView_friends);

        /* Inflate the Listview with the friendsName*/
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(ChooseFriendsActivity.this, android.R.layout.simple_list_item_multiple_choice, friendsListName);
        friendsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        friendsListView.setAdapter(arrayAdapter);
    }

    /**
     * This method existing to be sure that the Activity is killed when backButton is pressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    /**
     * Button to save friends who are taking part of the event
     * @param v
     */
    public void btn_get_participants(View v) {
       Log.i ("friend", "participant à l'événement: ");
        SparseBooleanArray checked = friendsListView.getCheckedItemPositions();
        Log.i ("friend", "numero checked: "+checked);
    }

    /**
     * Button to comeback to the previous Activity (here Creation Event Activity
     * @param v
     */
    public void btn_cancel(View v) {
        finish();
    }

}
