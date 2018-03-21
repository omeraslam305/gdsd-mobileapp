package com.example.omer.testapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.omer.testapplication.api.BackendAPI;
import com.example.omer.testapplication.api.Models.User;
import com.example.omer.testapplication.classes.Session;

public class ProfileActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etMobile, etEmail, etAddress;
    private Button btnUpdate;
    private ProgressDialog pDialog;
    private EditProfileTask mAuthTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etFirstName = (EditText) findViewById(R.id.input_firstname);
        etLastName = (EditText) findViewById(R.id.input_lastname);
        etEmail = (EditText) findViewById(R.id.input_email);
        etMobile = (EditText) findViewById(R.id.input_mobile);
        etAddress = (EditText) findViewById(R.id.input_address);
        pDialog = new ProgressDialog(ProfileActivity.this);


        btnUpdate = (Button) findViewById(R.id.btn_updateprofile);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }

    private  void showProgress(boolean show){
        if (show) {
            pDialog.setMessage("Updating information. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        else{
            pDialog.dismiss();
        }
    }

    private void updateProfile() {
        if (mAuthTask != null) {
            return;
        }

        String fname = etFirstName.getText().toString();
        String lname = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etMobile.getText().toString();
        String address = etAddress.getText().toString();

        showProgress(true);
        mAuthTask = new EditProfileTask(fname, lname ,email, phone, address);
        mAuthTask.execute((Void) null);
    }

    public class EditProfileTask extends AsyncTask<Void, Void, Boolean> {

        private final String mFname;
        private final String mLname;
        private final String mEmail;
        private final String mMobile;
        private final String mAddress;

        EditProfileTask(String fname, String lname ,String email, String phone, String address) {
            mFname = fname;
            mLname = lname;
            mEmail = email;
            mMobile = phone;
            mAddress = address;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            BackendAPI backendAPI = new BackendAPI();
            String userId = Integer.toString(Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0)).UserId);
            User user = backendAPI.updateProfile(userId, mFname,mLname,mEmail,mMobile,mAddress);
            if (user != null){
                SharedPreferences settings = getSharedPreferences(Session.getInstance().PREFS_NAME, 0);
                Session.getInstance().setUserInfo(user, settings);
            }
            // TODO: register the new account here.
            return user != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(ProfileActivity.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ProfileActivity.this,"Profile update failed",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
