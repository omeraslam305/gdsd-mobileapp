package com.example.omer.testapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omer.testapplication.api.BackendAPI;
import com.example.omer.testapplication.api.Models.RealEstateAd;
import com.example.omer.testapplication.api.Models.User;
import com.example.omer.testapplication.classes.Adapters.ConversationModel;
import com.example.omer.testapplication.classes.Adapters.RealEstateAdAdapter;
import com.example.omer.testapplication.classes.Adapters.RealEstateAdModel;
import com.example.omer.testapplication.classes.Session;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    EditText etSearchTxt;
    ImageView searchBtn;
    BackendAPI backendAPI;
    private AdRenderingTask mAuthTask = null;
    private ProgressDialog pDialog;
    ListView listView;
    ArrayList<RealEstateAd> dataModels;
    private static RealEstateAdAdapter adapter;
    ImageView imgUser;
    TextView tvUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backendAPI = new BackendAPI();
        pDialog = new ProgressDialog(DashboardActivity.this);
        listView=(ListView)findViewById(R.id.list_ads);
        //Toast.makeText(this, "OnCreate: Session: " + Session.getInstance().isUserInSession(getSharedPreferences(PREFS_NAME, 0)), Toast.LENGTH_LONG).show();



        etSearchTxt = (EditText) findViewById(R.id.et_search);
        searchBtn = (ImageView) findViewById(R.id.img_search_ad);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataModels.clear();
                fetchAdList(etSearchTxt.getText().toString(),"","","","");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        imgUser = (ImageView)hView.findViewById(R.id.imageViewUser);
        tvUsername = (TextView) hView.findViewById(R.id.textViewUserName);
        manageNavigationDrawerItems(Session.getInstance().isUserInSession(getSharedPreferences(Session.getInstance().PREFS_NAME, 0)));

        if (getIntent().getStringExtra("navigationFrom") != ""){
            String keyword = getIntent().getStringExtra("keyword");
            String accomodation = getIntent().getStringExtra("accomodation");
            String bedrooms = getIntent().getStringExtra("bedrooms");
            String sqft = getIntent().getStringExtra("sqft");
            String adType = getIntent().getStringExtra("adType");

            fetchAdList(keyword,accomodation,bedrooms,sqft,adType);
        } else{
            fetchAdList(etSearchTxt.getText().toString(),"","","","");
        }

    }

    public void fetchAdList(String searchText, String typeOfAccomodation, String noOfBedRooms, String squareFeet, String adType){
        showProgress(true);
        mAuthTask = new AdRenderingTask(searchText, typeOfAccomodation,noOfBedRooms,squareFeet,adType);
        mAuthTask.execute((Void) null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        //Toast.makeText(this, "onResume: Session: " + Session.getInstance().isUserInSession(getSharedPreferences(PREFS_NAME, 0)), Toast.LENGTH_LONG).show();
        manageNavigationDrawerItems(Session.getInstance().isUserInSession(getSharedPreferences(Session.getInstance().PREFS_NAME, 0)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(DashboardActivity.this,DashboardActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(DashboardActivity.this,AdvancedSearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_favourites) {
            Intent intent = new Intent(DashboardActivity.this,FavouriteAdActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(DashboardActivity.this,ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(DashboardActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_messages) {
            Intent intent = new Intent(DashboardActivity.this,UserConversationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Session.getInstance().clearSession(getSharedPreferences(Session.getInstance().PREFS_NAME, 0));
            Intent intent = new Intent(DashboardActivity.this,DashboardActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void manageNavigationDrawerItems(boolean isLoggedIn){
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_favourites).setVisible(isLoggedIn);
        nav_Menu.findItem(R.id.nav_profile).setVisible(isLoggedIn);
        nav_Menu.findItem(R.id.nav_login).setVisible(!isLoggedIn);
        nav_Menu.findItem(R.id.nav_messages).setVisible(isLoggedIn);
        nav_Menu.findItem(R.id.nav_logout).setVisible(isLoggedIn);
        if(isLoggedIn){
            User userInfo = Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0));
            tvUsername.setText(userInfo.FirstName + " " + userInfo.LastName);
            String imgPath = "http://ec2-54-200-111-60.us-west-2.compute.amazonaws.com:3000/" + userInfo.UserImagePath;
            Picasso.with(this).load(imgPath).fit().into(imgUser);
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

        private final String mSearchText;
        private final String mTypeOfAccomodation;
        private final String mNoOfBedRooms;
        private final String mSquareFeet;
        private final String mAdType;

        AdRenderingTask(String searchText, String typeOfAccomodation, String noOfBedRooms, String squareFeet, String adType) {
            mSearchText = searchText;
            mTypeOfAccomodation = typeOfAccomodation;
            mNoOfBedRooms = noOfBedRooms;
            mSquareFeet = squareFeet;
            mAdType = adType;
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
                dataModels = backendAPI.getRealEstateList(mSearchText,mTypeOfAccomodation,mNoOfBedRooms,mSquareFeet,mAdType);
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
        adapter= new RealEstateAdAdapter(adModel,DashboardActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RealEstateAdModel obj = (RealEstateAdModel)parent.getAdapter().getItem(position);
                Intent intent = new Intent(DashboardActivity.this,AdDetailActivity.class);
                intent.putExtra("listingId", Integer.toString(obj.getId()));
                startActivity(intent);

            }
        });

    }
}
