package edu.sjsu.snappychat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import edu.sjsu.snappychat.service.UserService;

import static android.R.attr.data;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextView info;
    private ImageView fb;
    private ImageView google;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        fb = (ImageView) findViewById(R.id.facebook);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(
                        LoginActivity.this,
                        Arrays.asList("user_photos", "email", "user_birthday", "public_profile")
                );
            }
        });

        google = (ImageView) findViewById(R.id.google);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback< LoginResult >() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                        String accessToken = loginResult.getAccessToken()
                                .getToken();
                        Log.i("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {@Override
                                public void onCompleted(JSONObject object,
                                                        GraphResponse response) {

                                    Log.i("LoginActivity",
                                            response.toString());
                                    try {
                                        String id = object.getString("id");
                                        try {
                                            URL profile_pic = new URL(
                                                    "http://graph.facebook.com/" + id + "/picture?type=large");
                                            Log.i("profile_pic",
                                                    profile_pic + "");

                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        UserService.getInstance().setEmail(object.getString("email"));
                                        UserService.getInstance().setNickName(object.getString("name"));
                                        Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_LONG).show();
                                        startLandingActivity();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
                                    }
                                }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields",
                                "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "Login attempt failed.", Toast.LENGTH_LONG).show();
                    }
                }

        );

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, LoginActivity.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void startLandingActivity() {
        Intent edit_page = new Intent(LoginActivity.this, LandingPageActivity.class);
        startActivity(edit_page);
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
            GoogleSignInAccount acct = result.getSignInAccount();
            UserService.getInstance().setEmail(acct.getEmail());
            UserService.getInstance().setEmail(acct.getDisplayName());
            Toast.makeText(LoginActivity.this, "Email: " + UserService.getInstance().getEmail(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Toast.makeText(LoginActivity.this, "firebaseAuthWithGoogle:" + acct.getId(), Toast.LENGTH_LONG).show();
        UserService.getInstance().setEmail(acct.getEmail());
        UserService.getInstance().setEmail(acct.getDisplayName());
        Toast.makeText(LoginActivity.this, "Email: " + UserService.getInstance().getEmail(), Toast.LENGTH_LONG).show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(LoginActivity.this, "signInWithCredential:onComplete:" + task.isSuccessful(), Toast.LENGTH_LONG).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        startLandingActivity();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this, "Connection failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
