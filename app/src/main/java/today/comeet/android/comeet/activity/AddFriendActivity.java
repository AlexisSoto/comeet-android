package today.comeet.android.comeet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
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
import java.util.HashMap;
import java.util.List;

import today.comeet.android.comeet.R;

public class AddFriendActivity extends AppCompatActivity {

    private ListView friendsListView;
    private List<String> friendIds;
    private List<String> friendNames;
    /*
        in facebook object api, friends are stored as {id: object_id, title: id_facebook_of_the_friend }
        Here in the Hap map key = id_facebook_of_the_friend and value = object_id
        object_id is the facebook_id of the object and i needed when we DELETE it
     */
    HashMap<String, String> favoriteFriends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        friendIds = new ArrayList();
        friendNames = new ArrayList();
        favoriteFriends = new HashMap<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SparseBooleanArray checked = friendsListView.getCheckedItemPositions();
                for (int i = 0; i < friendIds.size(); i++) {
                    if(checked.get(i) == true ) {
                        if(favoriteFriends.get(friendIds.get(i)) == null) {
                            // not already a favorite friend
                            createFavoriteFriend(friendIds.get(i));
                        }
                    } else {
                        // not checked
                        if(favoriteFriends.get(friendIds.get(i)) != null) {
                            // was a favorite friend, so we delete him
                            deleteFavoriteFriend(favoriteFriends.get(friendIds.get(i)));
                        }
                    }
                }

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(AddFriendActivity.this, HomeActivity.class);
                startActivity(intent);
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
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject row = null;
                            try {
                                row = array.getJSONObject(i);
                                friendNames.add(row.getString("name"));
                                friendIds.add(row.getString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        // we have the user friends, so now we ask his favorite friends
                        GraphRequest request = new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "me/objects/comeetapplication:favorite_friend",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {
                                        Log.d("Favorite friend", "GET" );
                                        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(AddFriendActivity.this, android.R.layout.simple_list_item_multiple_choice, friendNames);
                                        friendsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                                        friendsListView.setAdapter(arrayAdapter);
                                        try {
                                            JSONArray array = response.getJSONObject().getJSONArray("data");
                                            JSONObject row = null;
                                            // for each favorite friend we check the corresponding item
                                            for (int i = 0; i < array.length(); i++) {
                                                row = array.getJSONObject(i);
                                                favoriteFriends.put(row.getString("title"), row.getString("id"));
                                                int index = friendIds.indexOf(row.getString("title"));
                                                if(index > -1) {
                                                    friendsListView.setItemChecked(index, true);
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                        );
                        request.executeAsync();
                    }
                });
        request.executeAsync();
    }

    private void createFavoriteFriend(String id) {
        Log.d("Favorite friend", "want to create " + id);
        Bundle params = new Bundle();
        params.putString("object", "{'og:title': " + id + "}");
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
    }

    private void deleteFavoriteFriend(String id) {
        // we POST a hard coded object (favorite friend)
        Log.d("Favorite friend", "want to delete " + id);
       // Bundle params = new Bundle();
       // params.putString("object", "{'og:title': " + id + "}");
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                id,
                null,
                HttpMethod.DELETE,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.d("Favorite friend", "deleted" );
                    }

                }
        );
        request.executeAsync();
    }

}
