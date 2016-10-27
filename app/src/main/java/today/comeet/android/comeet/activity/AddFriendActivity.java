package today.comeet.android.comeet.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import today.comeet.android.comeet.R;

public class AddFriendActivity extends AppCompatActivity {

    private ListView friendsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // we POST a hard coded object (favorite friend)
                Bundle params = new Bundle();
                params.putString("object", "{'og:title': 'Truc'}");
                GraphRequest request = new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "me/objects/comeetapplication:favorite_friend",
                    params,
                    HttpMethod.POST,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            Log.d("Favorite friend", "added" );
                        }

                    }
                );
                request.executeAsync();

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // in order to create custom object (favorite friend) we need the "publish_actions" permission
        LoginManager.getInstance().logInWithPublishPermissions(
                this,
                Arrays.asList("publish_actions"));


        friendsListView = (ListView) findViewById(R.id.listView_friends);

        GraphRequest request = GraphRequest.newMyFriendsRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray array, GraphResponse response) {
                        List<String> names = new ArrayList<String>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject row = null;
                            try {
                                row = array.getJSONObject(i);
                                names.add(row.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(AddFriendActivity.this, android.R.layout.simple_list_item_multiple_choice, names);
                        friendsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                        friendsListView.setAdapter(arrayAdapter);

                    }
                });

        request.executeAsync();

    }

}
