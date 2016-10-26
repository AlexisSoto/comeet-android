package today.comeet.android.comeet.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


import today.comeet.android.comeet.R;
import today.comeet.android.comeet.activity.HomeActivity;
import today.comeet.android.comeet.activity.MainActivity;
import today.comeet.android.comeet.helper.ApiHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class FbLoginFragment extends Fragment {

    private CallbackManager callbackManager;
    private AccessTokenTracker tokenTracker;
    private ProfileTracker profileTracker;
    private String[] userPermission = {"public_profile", "email", "user_birthday", "user_location", "user_friends"};

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            final AccessToken accessToken = loginResult.getAccessToken();
            Log.d("FBLogin", "User ID: "
                    + accessToken.getUserId()
                    + "\n" +
                    "Auth Token: "
                    + accessToken.getToken()
                    + "\n" +
                    "Permissions"
                    + accessToken.getPermissions());
            Profile profile = Profile.getCurrentProfile();

            if (accessToken.getPermissions().size() == userPermission.length) {
                Log.d("FBLogin", "testPermissionsWorking");
            }

            // Envoie du token au serveur.
            ApiHelper apihelper= new ApiHelper(getContext());
            apihelper.sendFbToken(accessToken.getToken());

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
        setupTokenTracker();
        setupProfileTracker();

        tokenTracker.startTracking();
        profileTracker.startTracking();
        callbackManager = CallbackManager.Factory.create();

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };
    }
    private void setupTokenTracker() {
        tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("FBLogin", "CurrentAccessToken" + currentAccessToken);
                // Si l'utilisateur se deconnecte
                if (currentAccessToken== null) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    private void updateWithToken(AccessToken currentAccessToken) {
        // Si token != null alors utilisateur connecté donc on charge HomeActivity
        if (currentAccessToken != null) {
            startActivity(new Intent(this.getContext(), HomeActivity.class));
        }
    }

    private void setupProfileTracker() {
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.d("CurrentProfile", "" + currentProfile);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_facebook, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(userPermission);
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization
        loginButton.registerCallback(callbackManager, facebookCallback);
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
