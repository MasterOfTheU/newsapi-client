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
import com.newsapiclient.R;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignupActivity";

    private FirebaseAuth mAuth;

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mPasswordConfirmField;
    private Button mBtnSignup;
    private TextView mTextSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();

        mAuth = FirebaseAuth.getInstance();
    }

    private void initViews() {
        mEmailField = findViewById(R.id.signup_email);
        mPasswordField = findViewById(R.id.signup_password);
        mPasswordConfirmField = findViewById(R.id.signup_password_confirm);
        mBtnSignup = findViewById(R.id.btn_signup);
        mTextSignin = findViewById(R.id.text_signin);

        mBtnSignup.setOnClickListener(this);
        mTextSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.text_signin) {
            showLoginScreen();
        } else if (id == R.id.btn_signup) {
            registerUser();
        }
    }

    private void showLoginScreen() {
        Log.d(TAG, "showLoginScreen: Redirecting to login screen");
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void registerUser() {
        if (!(validateForm())) {
            Log.d(TAG, "registerUser: Form is invalid");
            displayToast("Form is invalid. Check out the fields and try again.");
            setResult(RESULT_CANCELED);
            finish();
        } else {
            Log.d(TAG, "registerUser: Creating an account");
            String email = mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();
            createAccount(email, password);
            setResult(RESULT_OK);
            finish();
        }
    }

    private boolean validateForm() {
        Log.d(TAG, "validateForm: Validating form fields");

        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (email.isEmpty()) {
            mEmailField.setError("Field is required");
            valid = false;
        }

        String password = mPasswordField.getText().toString();
        if (password.isEmpty()) {
            mPasswordField.setError("Field is required");
            valid = false;
        }

        String passwordConfirm = mPasswordConfirmField.getText().toString();
        if (passwordConfirm.isEmpty()) {
            displayToast("Please, confirm password!");
            valid = false;
        }

        if (!email.isEmpty() && !password.isEmpty() && !passwordConfirm.isEmpty()) {
            if (!password.equals(passwordConfirm)) {
                mPasswordConfirmField.setError("Check password and try again");
                valid = false;
            }
        }

        return valid;
    }


    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUser: success");
                        } else {
                            Log.w(TAG, "createUser: failure ", task.getException());
                            displayToast("Registration failed.");
                        }
                    }
                });
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
