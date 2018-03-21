package com.example.omer.testapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.omer.testapplication.api.BackendAPI;
import com.example.omer.testapplication.api.Models.User;
import com.example.omer.testapplication.classes.Session;

public class SignUpActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etMobile, etEmail, etUsername, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private ProgressDialog pDialog;
    private UserSignupTask mAuthTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etFirstName = (EditText) findViewById(R.id.input_firstname);
        etLastName = (EditText) findViewById(R.id.input_lastname);
        etEmail = (EditText) findViewById(R.id.input_email);
        etUsername = (EditText) findViewById(R.id.input_username);
        etMobile = (EditText) findViewById(R.id.input_mobile);
        etPassword = (EditText) findViewById(R.id.input_password);
        etConfirmPassword = (EditText) findViewById(R.id.input_cpassword);
        pDialog = new ProgressDialog(SignUpActivity.this);


        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });
    }

    private  void showProgress(boolean show){
        if (show) {
            pDialog.setMessage("Sigining up user. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        else{
            pDialog.dismiss();
        }
    }

    private void attemptSignup() {
        if (mAuthTask != null) {
            return;
        }

        String fname = etFirstName.getText().toString();
        String lname = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etMobile.getText().toString();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String cpassword = etConfirmPassword.getText().toString();

        if (fname.equals("")) {
            Toast.makeText(this,"Please enter First Name",Toast.LENGTH_LONG).show();
            return;
        }else if (lname.equals("")) {
            Toast.makeText(this,"Please enter Last Name",Toast.LENGTH_LONG).show();
            return;
        }else if (email.equals("")) {
            Toast.makeText(this,"Please enter Email",Toast.LENGTH_LONG).show();
            return;
        }else if (phone.equals("")) {
            Toast.makeText(this,"Please enter Phone",Toast.LENGTH_LONG).show();
            return;
        }else if (username.equals("")) {
            Toast.makeText(this,"Please enter Username",Toast.LENGTH_LONG).show();
            return;
        }else if (password.equals("")) {
            Toast.makeText(this,"Please enter Password",Toast.LENGTH_LONG).show();
            return;
        }else if (cpassword.equals("")) {
            Toast.makeText(this,"Please enter Confirm Password",Toast.LENGTH_LONG).show();
            return;
        }else if (!cpassword.equals(password)) {
            Toast.makeText(this,"Confirm Password doesn't match",Toast.LENGTH_LONG).show();
            return;
        } else {
            showProgress(true);
            mAuthTask = new UserSignupTask(fname, lname ,email, phone, username, password, cpassword);
            mAuthTask.execute((Void) null);
        }
    }

    public class UserSignupTask extends AsyncTask<Void, Void, Boolean> {

        private final String mFname;
        private final String mLname;
        private final String mEmail;
        private final String mMobile;
        private final String mUsername;
        private final String mCpassword;
        private final String mPassword;

        UserSignupTask(String fname, String lname ,String email, String phone, String username, String password, String cpassword) {
            mFname = fname;
            mLname = lname;
            mEmail = email;
            mMobile = phone;
            mUsername = username;
            mCpassword = cpassword;
            mPassword = password;
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
            User user = backendAPI.registerUser(mFname,mLname,mEmail,mMobile,mUsername,mPassword,mCpassword);
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
                Intent intent = new Intent(SignUpActivity.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
