package com.newsapiclient.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.newsapiclient.R;
import com.newsapiclient.client.HolderActivity;
import com.newsapiclient.util.Constants;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private boolean mExistsInDatabase;
    private String mExistsInDatabaseEmail;
    private boolean mProfileIsSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mExistsInDatabase = ClientApplication.getPreferenceDataBoolean("exists_in_db");
        mExistsInDatabaseEmail = ClientApplication.getPreferenceDataString("email");
        mProfileIsSetup = ClientApplication.getPreferenceDataBoolean("profile_setup");

        Log.d(TAG, "onCreate: mExistsInDatabase: " + mExistsInDatabase);
        Log.d(TAG, "onCreate: mExistsInDatabaseEmail: " + mExistsInDatabaseEmail);
        Log.d(TAG, "onCreate: mProfileIsSetup: " + mProfileIsSetup);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    if (mExistsInDatabaseEmail.equals(currentUser.getEmail())) {
                        Log.d(TAG, "User " + currentUser.getEmail() + " has logged in");
                        if (!mProfileIsSetup) {
                            Log.d(TAG, "User hasn't setup the profile yet ");
                            showProfileSetupScreen();
                        } else {
                            updateUI();
                        }
                    }
                } else {
                    Log.d(TAG, "onAuthStateChanged: Checking if user exists in db");
                    if (mExistsInDatabase) {
                        Log.d(TAG, "onAuthStateChanged: Show login screen");
                        showLoginScreen();
                    } else {
                        Log.d(TAG, "onAuthStateChanged: Registering user");
                        showSignupScreen();
                    }
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Login request processed");
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    if (!mProfileIsSetup) {
                        showProfileSetupScreen();
                    } else {
                        updateUI();
                    }
                }
            } else {
                displayToast("Login failed");
                showLoginScreen();
            }
        } else if (requestCode == Constants.RC_SIGN_UP) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: Sign up request processed");
                displayToast("Registration complete");
                showLoginScreen();
            } else {
                displayToast("Registration failed");
                showSignupScreen();
            }
        }
    }

    private void showProfileSetupScreen() {
        Log.d(TAG, "showProfileSetupScreen: Redirecting");
        Intent profileSetupIntent =
                new Intent(this, ProfileSetupActivity.class);
        startActivity(profileSetupIntent);
        finish();
    }

    private void showSignupScreen() {
        Log.d(TAG, "showSignupScreen: Redirecting");
        Intent signupIntent = new Intent(this, SignupActivity.class);
        startActivityForResult(signupIntent, Constants.RC_SIGN_UP);
    }

    private void showLoginScreen() {
        Log.d(TAG, "showLoginScreen: Redirecting");
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, Constants.RC_SIGN_IN);
    }

    private void updateUI() {
        Log.d(TAG, "updateUI: Redirecting to home screen");
        Intent homeIntent = new Intent(this, HolderActivity.class);
        startActivity(homeIntent);
        finish();
    }

    /**
     * A customizable toast message.
     *
     * @param message Message to be displayed
     */
    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

}
