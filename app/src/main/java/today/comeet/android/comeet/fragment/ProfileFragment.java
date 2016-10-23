package today.comeet.android.comeet.fragment;

/**
 * Created by Vincent on 11/10/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import today.comeet.android.comeet.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView profileName;
    private ImageView profilePicture;

    public static ProfileFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(args);
        return profileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        profileName = (TextView)rootView.findViewById(R.id.profile_name);
        profilePicture = (ImageView) rootView.findViewById(R.id.profile_picture);

        // we will query the name with the openGraph API
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // this code is executed when the response from the API is received (async)
                        try {
                            profileName.setText(object.getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        // we pass the right parameters for the query : fields=name
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name");
        request.setParameters(parameters);
        request.executeAsync(); // execute the query


        // We will query the profile picture
        GraphRequest pictureRequest = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/picture",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse pictureResponse) {
                        try {
                            Picasso.with(getContext()).load(pictureResponse.getJSONObject().getJSONObject("data").getString("url")).into(profilePicture);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle pictureParameters = new Bundle();
        pictureParameters.putString("redirect", "false");
        pictureRequest.setParameters(pictureParameters);
        pictureRequest.executeAsync();



        // Inflate the layout for this fragment
        return rootView;
    }

}