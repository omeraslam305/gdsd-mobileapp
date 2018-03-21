package com.example.omer.testapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.omer.testapplication.api.BackendAPI;
import com.example.omer.testapplication.api.Models.RealEstateAd;
import com.example.omer.testapplication.api.Models.User;
import com.example.omer.testapplication.classes.Adapters.RealEstateAdAdapter;
import com.example.omer.testapplication.classes.Adapters.RealEstateAdModel;
import com.example.omer.testapplication.classes.Session;

import java.util.ArrayList;

public class FavouriteAdActivity extends AppCompatActivity {
    BackendAPI backendAPI;
    private AdRenderingTask mAuthTask = null;
    private ProgressDialog pDialog;
    ListView listView;
    ArrayList<RealEstateAd> dataModels;
    private static RealEstateAdAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_ad);
        backendAPI = new BackendAPI();
        pDialog = new ProgressDialog(FavouriteAdActivity.this);
        listView=(ListView)findViewById(R.id.list_ads);

        showProgress(true);
        if(Session.getInstance().isUserInSession(getSharedPreferences(Session.getInstance().PREFS_NAME, 0))){
            User userInfo = Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0));
            mAuthTask = new AdRenderingTask(Integer.toString(userInfo.UserId),Integer.toString(userInfo.UserTypeId));
            mAuthTask.execute((Void) null);
        }
    }

    private  void showProgress(boolean show){
        if (show) {
            pDialog.setMessage("Loading Ads. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        else{
            pDialog.dismiss();
        }
    }

    public class AdRenderingTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserId;
        private final String mUserType;

        AdRenderingTask(String userId, String userType) {
            mUserId = userId;
            mUserType = userType;
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
                BackendAPI backendAPI = new BackendAPI();
                dataModels = backendAPI.getFavouriteAds(mUserId,mUserType);
            } catch (Exception e) {
                return false;
            }
            // TODO: register the new account here.
            return dataModels != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            renderList();
            if (success) {

            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public void renderList(){
        ArrayList<RealEstateAdModel> adModel = new ArrayList<RealEstateAdModel>();
        for (RealEstateAd ad : dataModels){
            adModel.add(new RealEstateAdModel(ad.Id,ad.Title,ad.Price,ad.City + " ," +ad.State+ " ," +ad.Zip,(ad.AdStatusId == 1 ? "Available" : "Sold Out"),ad.AdMedia.get(0).ImagePath));
        }
        adapter= new RealEstateAdAdapter(adModel,FavouriteAdActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(FavouriteAdActivity.this, "You Clicked at ", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
