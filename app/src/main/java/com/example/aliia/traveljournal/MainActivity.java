package com.example.aliia.traveljournal;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken facebookAccessToken;
    private Button facebookbutton;
    private FirebaseAuth firebaseAuth;
    private SignInButton googlesignin;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser currentuser= firebaseAuth.getCurrentUser();

        if(currentuser==null)
        {
            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.activity_main);
            loginButton=(LoginButton)findViewById(R.id.login_button);

            callbackManager=CallbackManager.Factory.create();
            loginButton.setReadPermissions(Arrays.asList("email"));


            googlesignin=findViewById(R.id.sign_in_button);
            // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            googlesignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn();
                }
            });
        }
        else
        {
            Intent intent=new Intent (MainActivity.this,timeline.class);
            startActivity(intent);
        }






    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Main Activity", "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Main Activity", "Google sign in failed", e);
                // ...
            }
        }else{
            //If not request code is RC_SIGN_IN it must be facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Main Activity", "Google sign in failed", e);
                // ...
            }
        }
    }
    */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Main Activity", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Main Activity", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Main Activity", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this,"You are not able to log in to google",Toast.LENGTH_LONG).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


    public void buttonclickLoginFb(View v)
    {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"User cancelled it",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();


            }
        });
    }
    private void handleFacebookToken(AccessToken accessToken) {

        AuthCredential credential=FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {

                            FirebaseUser myuserobj= firebaseAuth.getCurrentUser();
                            //myuserobj
                            updateUI();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Couldn't register to firebase",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


   /*
    private void initializeFacebookLogin(){

        callbackManager=CallbackManager.Factory.create();
        loginButton.setReadPermissions("email","public_profile","user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Result","facebook:onSuccess"+loginResult);
                facebookAccessToken=loginResult.getAccessToken();
                handleFacebookAccessToken(facebookAccessToken);
            }

            @Override
            public void onCancel() {
                Log.d("Result","facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Result","facebook:onError",error);

            }
        });
    }


/*
*/
   /*
    public void handleFacebookAccessToken(AccessToken token)
    {
        AuthCredential credential= FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            FirebaseUser user=firebaseAuth.getCurrentUser();



                            Intent intent = new Intent(MainActivity.this, timeline.class);

                            startActivity(intent);
                            //changeToActivity2();
                        }
                        else
                        {

                        }
                    }
                });

    }*/
    public void changeToActivity2()
    {

        Intent intent=new Intent(MainActivity.this,timeline.class);
        startActivity(intent);

    }
    private void updateUI() {

        Toast.makeText(MainActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, profile.class);

        startActivity(intent);
        finish();
        Log.d("FinalResult","End");


    }



    /*public void fblogin(View view)
    {
        Intent intent = new Intent(this, timeline.class);
        startActivity(intent);
        finish();
    }
    /*
    private void printkeyhash()
    {
        try
        {

            PackageInfo info=getPackageManager().getPackageInfo("com.example.aliia.traveljournal",getPackageManager().GET_SIGNATURES);
            for(Signature signature:info.signatures)
            {
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }

        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }*/
    public void loginpage(View view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
    public void signuppage(View view)
    {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);

    }
   /* public void fbdatapage()
    {
        Intent intent = new Intent(this, facebookdata.class);
        startActivity(intent);
    }*/
}