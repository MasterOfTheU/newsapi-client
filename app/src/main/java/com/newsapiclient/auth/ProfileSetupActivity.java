package com.newsapiclient.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.newsapiclient.R;
import com.newsapiclient.ui.HomeActivity;
import com.newsapiclient.util.Converters;
import com.newsapiclient.util.DatePickerFragment;

import java.util.Date;


public class ProfileSetupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProfileSetupActivity";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mPhoneNumberField;
    private EditText mBirthDateField;
    private Button mBtnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseDatabase.getReference("users");

        initViews();
    }

    private void initViews() {
        mFirstNameField = findViewById(R.id.input_firstname);
        mLastNameField = findViewById(R.id.input_lastname);
        mPhoneNumberField = findViewById(R.id.input_phone);

        mBirthDateField = findViewById(R.id.input_birthday);
        mBirthDateField.setOnClickListener(this);

        mBtnFinish = findViewById(R.id.btn_finish);
        mBtnFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.input_birthday) {
            showDatePickerDialog();
        } else if (id == R.id.btn_finish) {
            updateUserProfile();
            showHomeScreen();
        }
    }

    private void showHomeScreen() {
        Intent homeIntent = new Intent(ProfileSetupActivity.this, HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }

    private void updateUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String fullName =
                Converters.getFullName(mFirstNameField.getText().toString(),
                        mLastNameField.getText().toString());
        String phoneNumber = mPhoneNumberField.getText().toString();
        String birthday = mBirthDateField.getText().toString();

        if (!validateForm()) {
            displayToast("All fields are required");
        } else {
            if (currentUser != null) {
                Log.d(TAG, "updateUserProfile: saving " + currentUser.getEmail()
                        + " to firebase: \n" + fullName + " " + phoneNumber + " " + birthday);
                DatabaseReference userReference = mDatabase.child("uid");
                userReference.child("name").setValue(fullName);
                userReference.child("phone").setValue(phoneNumber);
                userReference.child("birthday").setValue(birthday);

                ClientApplication.setPreferencesBoolean("profile_setup", true);
            }
        }
    }

    private boolean validateForm() {
        Log.d(TAG, "validateForm: Validating form fields");

        boolean valid = true;

        String firstName = mFirstNameField.getText().toString();
        if (firstName.isEmpty()) {
            mFirstNameField.setError("Field is required");
            valid = false;
        } else {
            mFirstNameField.setError(null);
        }

        String lastName = mLastNameField.getText().toString();
        if (lastName.isEmpty()) {
            mLastNameField.setError("Field is required");
            valid = false;
        } else {
            mLastNameField.setError(null);
        }

        String phoneNumber = mPhoneNumberField.getText().toString();
        if (phoneNumber.isEmpty()) {
            mPhoneNumberField.setError("Field is required");
            valid = false;
        } else {
            mPhoneNumberField.setError(null);
        }

        String birthday = mBirthDateField.getText().toString();
        if (birthday.isEmpty()) {
            mBirthDateField.setError("Field is required");
            valid = false;
        } else {
            mBirthDateField.setError(null);
        }

        return valid;
    }

    private void showDatePickerDialog() {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void processDatePickerResult(int year, int month, int day) {
        String date = Converters.getDateString(year, month, day);

        Date pickedDate = Converters.getDateFromString(date);
        Date now = new Date();

        if (now.before(pickedDate)) {
            Log.d(TAG, "processDatePickerResult: Picked date is in the future");
            mBirthDateField.setText("");
            displayToast("The date you entered is in the future. Pick another date.");
        } else {
            mBirthDateField.setText(date);
            mBirthDateField.setFocusable(true);
        }

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
