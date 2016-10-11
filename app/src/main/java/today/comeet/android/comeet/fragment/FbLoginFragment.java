package today.comeet.android.comeet.fragment;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import today.comeet.android.comeet.R;
import today.comeet.android.comeet.activity.HomeActivity;

import static android.R.id.list;

/**
 * A placeholder fragment containing a simple view.
 */
public class FbLoginFragment extends Fragment {

    private TextView textDetails;

    private CallbackManager callbackManager;
    private AccessTokenTracker tokenTracker;
    private ProfileTracker profileTracker;
    private String[] userPermission = {"public_profile", "email", "user_birthday", "user_location"};

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Log.d("FBLogin", "User ID: "
                    + accessToken.getUserId()
                    + "\n" +
                    "Auth Token: "
                    + accessToken.getToken()
                    + "\n" +
                    "Permissions"
                    + accessToken.getPermissions());
            Profile profile = Profile.getCurrentProfile();

            if(accessToken.getPermissions().size()==userPermission.length){
                Log.d("FBLogin", "testPermissionsWorking");
            }
            if(profile != null){
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onCancel() {
            Log.d("FBLogin", "onCancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("FBLogin", "onError");
        }
    };

    public FbLoginFragment(){
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        setupTokenTracker();
        setupProfileTracker();

        tokenTracker.startTracking();
        profileTracker.startTracking();
        callbackManager = CallbackManager.Factory.create();
    }
    private void setupTokenTracker() {
        tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("FBLogin", "CurrentAccessToken" + currentAccessToken);
            }
        };
    }

    private void setupProfileTracker() {
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.d("CurrentProfile", "" + currentProfile);
                textDetails.setText(constructWelcomeMessage(currentProfile));
            }
        };
    }

    private String constructWelcomeMessage(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (profile != null) {
            stringBuffer.append("Welcome " + profile.getName());
        }
        return stringBuffer.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(userPermission);
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization
        loginButton.registerCallback(callbackManager, facebookCallback);

        textDetails = (TextView) view.findViewById(R.id.text_details);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }
}
