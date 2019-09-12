package com.newsapiclient.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newsapiclient.R;
import com.newsapiclient.model.User;
import com.newsapiclient.util.Constants;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mBtnSignin;
    private TextView mTextSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseDatabase.getReference("users");

        initViews();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.text_signup) {
            showSignupScreen();
        } else if (id == R.id.btn_signin) {
            loginUser();
            setResult(RESULT_OK);
            finish();
        }
    }

    private void initViews() {
        mEmailField = findViewById(R.id.login_email);
        mPasswordField = findViewById(R.id.login_password);

        mBtnSignin = findViewById(R.id.btn_signin);
        mTextSignup = findViewById(R.id.text_signup);

        mBtnSignin.setOnClickListener(this);
        mTextSignup.setOnClickListener(this);
    }

    private void saveUserInDb(@NonNull FirebaseUser currentUser) {
        Log.d(TAG, "saveUserInDb: saving current user to realtime database");

        final User user = new User(currentUser.getUid(),
                currentUser.getEmail(),
                currentUser.getDisplayName());

        if (!existsInDatabase(currentUser)) {
            mDatabase.child("uid").setValue(user);
            // Those values are set later after profile setup
            mDatabase.child("uid").child("name").setValue("");
            mDatabase.child("uid").child("phone").setValue("");
            mDatabase.child("uid").child("birthday").setValue("");
            saveUserPreferencesExistsInDb(currentUser.getEmail());
        }
    }

    private void saveUserPreferencesExistsInDb(String email) {
        Log.d(TAG, "Writing values to SharedPrefs");
        ClientApplication.setPreferencesString("email", email);
        ClientApplication.setPreferencesBoolean("exists_in_db", true);
    }

    /**
     * @param currentUser Authenticated user
     * @return <b>true</b> - if current user's email is equal to email that is stored
     * in database
     */
    private boolean existsInDatabase(@NonNull FirebaseUser currentUser) {
        final boolean[] flag = new boolean[1];
        mDatabase.child("users").child("uid").orderByChild("email")
                .equalTo(currentUser.getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        flag[0] = dataSnapshot.exists();
                        Log.d(TAG, "onDataChange: User exists.");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "onDataChange: Failed to read user info. \n"
                                + databaseError.toException());
                    }
                });
        return flag[0];
    }

    private void showSignupScreen() {
        Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivityForResult(signupIntent, Constants.RC_SIGN_UP);
    }

    private void loginUser() {
        if (!(validateForm())) {
            Log.d(TAG, "signIn: Form is invalid");
            displayToast("Form is invalid. Check out the fields and try again.");
            setResult(RESULT_CANCELED);
            finish();
        } else {
            Log.d(TAG, "signIn: Loggin into account");
            String email = mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();
            signIn(email, password);

        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail: success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                saveUserInDb(currentUser);
                                saveUserPreferencesExistsInDb(currentUser.getEmail());
                            }
                        } else {
                            Log.w(TAG, "signInWithEmail: failure", task.getException());
                        }
                    }
                });
    }

    private boolean validateForm() {
        Log.d(TAG, "validateForm: Validating form fields");

        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (email.isEmpty()) {
            mEmailField.setError("Field is required");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (password.isEmpty()) {
            mPasswordField.setError("Field is required");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
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
