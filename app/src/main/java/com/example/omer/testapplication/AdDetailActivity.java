package com.example.omer.testapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omer.testapplication.api.BackendAPI;
import com.example.omer.testapplication.api.Models.AdDetails;
import com.example.omer.testapplication.api.Models.AdvertisementMedia;
import com.example.omer.testapplication.api.Models.RealEstateAd;
import com.example.omer.testapplication.api.Models.UserConversationModel;
import com.example.omer.testapplication.classes.Adapters.ConversationModel;
import com.example.omer.testapplication.classes.Adapters.RealEstateAdAdapter;
import com.example.omer.testapplication.classes.Adapters.RealEstateAdModel;
import com.example.omer.testapplication.classes.Adapters.ViewPagerAdapter;
import com.example.omer.testapplication.classes.Session;
import com.example.omer.testapplication.classes.SliderUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private AdDetailTask mAuthTask = null;
    private ProgressDialog pDialog;
    public AdDetails dataModel;
    String listingId;
    ViewPager viewPager;
    List<SliderUtils> sliderImg;
    ViewPagerAdapter viewPagerAdapter;
    ImageView agentImage;
    TextView tvAdTitle, tvAdDescription, tvAdBed, tvBathroom, tvKitchen, tvArea, tvParking, tvType, tvPrice, tvFloors, tvLivingRooms, tvLotArea, tvAddress, tvAgentName;
    EditText etMsg;
    Button btnSendMsg, btnMarkFav, btnUnmarkFav;
    LinearLayout agentContainer, msgContainer, favContainer;
    private PostMessageTask mPMTask = null;
    private FavTask mFavTask = null;
    String userId;
    double lat =  50.5558095, lang = 9.680844900000011;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ad_detail);

            pDialog = new ProgressDialog(AdDetailActivity.this);
            listingId = getIntent().getStringExtra("listingId");

            sliderImg = new ArrayList<>();

            viewPager = (ViewPager) findViewById(R.id.viewPager);
            agentImage = (ImageView) findViewById(R.id.img_agent);

            tvAdTitle = (TextView) findViewById(R.id.tv_ad_title);
            tvAdDescription = (TextView) findViewById(R.id.tv_ad_desc);
            tvAdBed = (TextView) findViewById(R.id.tv_ad_bed);
            tvBathroom = (TextView) findViewById(R.id.tv_ad_bath);
            tvKitchen = (TextView) findViewById(R.id.tv_ad_kitchen);
            tvArea = (TextView) findViewById(R.id.tv_ad_area);
            tvParking = (TextView) findViewById(R.id.tv_ad_parking);
            tvType = (TextView) findViewById(R.id.tv_ad_type);
            tvPrice = (TextView) findViewById(R.id.tv_ad_price);
            tvFloors = (TextView) findViewById(R.id.tv_ad_floors);
            tvLivingRooms = (TextView) findViewById(R.id.tv_ad_lvrooms);
            tvLotArea = (TextView) findViewById(R.id.tv_ad_lot_area);
            tvAddress = (TextView) findViewById(R.id.tv_ad_address);
            tvAgentName = (TextView) findViewById(R.id.tv_ad_agent_name);

            etMsg = (EditText) findViewById(R.id.et_agent_msg);
            btnSendMsg = (Button) findViewById(R.id.btn_send_msg);
            btnMarkFav = (Button) findViewById(R.id.btn_mark_fav);
            btnUnmarkFav = (Button) findViewById(R.id.btn_unmark_fav);

            userId = Integer.toString(Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0)).UserId);

            btnSendMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                String msgText = etMsg.getText().toString();
                mPMTask = new PostMessageTask(userId,Integer.toString(dataModel.AgentId),msgText);
                mPMTask.execute();
                }
            });

            btnMarkFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                mFavTask = new FavTask(true);
                mFavTask.execute();
                }
            });

            btnUnmarkFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                mFavTask = new FavTask(false);
                mFavTask.execute();
                }
            });

            agentContainer = (LinearLayout)findViewById(R.id.layout_agent);
            msgContainer = (LinearLayout) findViewById(R.id.layout_msg_agent);
            favContainer = (LinearLayout) findViewById(R.id.layout_favourite);

            manageFormVisibility();

            mAuthTask = new AdDetailTask(listingId);
            mAuthTask.execute();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng latLng = new LatLng(lat, lang);
        map.addMarker(new MarkerOptions().position(latLng).title("Fulda"));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 12.0f);
        map.animateCamera(yourLocation);
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

    public class AdDetailTask extends AsyncTask<Void, Void, Boolean> {

        private final String mListingId;

        AdDetailTask(String listingId) {
            mListingId = listingId;
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
                dataModel = BackendAPI.getAdDetail(mListingId);
            } catch (Exception e) {
                return false;
            }
            // TODO: register the new account here.
            return dataModel != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                renderAd();
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public void renderAd(){
        tvAdTitle.setText(dataModel.Title);
        tvAdDescription.setText(dataModel.AdDescription);
        tvAdBed.setText(dataModel.BedRooms);
        tvBathroom.setText(dataModel.BathRooms);
        tvKitchen.setText(dataModel.Kitchen);
        tvArea.setText(dataModel.SquareFeet);
        tvParking.setText(dataModel.Parking);
        tvType.setText(dataModel.AdType.AdTypeName);
        tvPrice.setText(dataModel.Price);
        tvFloors.setText(dataModel.NumOfFloors);
        tvLivingRooms.setText(dataModel.LivingRooms);
        tvLotArea.setText(dataModel.LotArea);
        tvAddress.setText(dataModel.Address);
        tvAgentName.setText(dataModel.AgentName);

        for (AdvertisementMedia image : dataModel.AdMedia){
            SliderUtils sliderUtils = new SliderUtils();
            sliderUtils.setSliderImageUrl(BackendAPI.baseAddress + image.ImagePath);
            sliderImg.add(sliderUtils);
        }

        String imgPath = BackendAPI.baseAddress + dataModel.AgentImage;
        Picasso.with(AdDetailActivity.this).load(imgPath).fit().into(agentImage);
        viewPagerAdapter = new ViewPagerAdapter(sliderImg, AdDetailActivity.this);

        viewPager.setAdapter(viewPagerAdapter);

        lat = Double.parseDouble(dataModel.Latitude);
        lang = Double.parseDouble(dataModel.Longitude);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        int userTypeId = Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0)).UserTypeId;
        if(userTypeId == 1){ //Customer
            Boolean isAdFavourite = dataModel.FavouriteAds.contains(listingId);
            if(isAdFavourite){
                btnMarkFav.setVisibility(View.GONE);
                btnUnmarkFav.setVisibility(View.VISIBLE);
            }else{
                btnMarkFav.setVisibility(View.VISIBLE);
                btnUnmarkFav.setVisibility(View.GONE);
            }
        }
    }

    public void manageFormVisibility(){
        if(Session.getInstance().isUserInSession(getSharedPreferences(Session.getInstance().PREFS_NAME, 0))){
            int userTypeId = Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0)).UserTypeId;
            msgContainer.setVisibility(userTypeId == 1 ? View.VISIBLE : View.GONE);
            favContainer.setVisibility(userTypeId == 1 ? View.VISIBLE : View.GONE);
        }
    }

    public class PostMessageTask extends AsyncTask<Void, Void, Boolean> {

        private final String mSenderId;
        private final String mReceiverId;
        private final String mMsgText;

        PostMessageTask(String senderId,String receiverId, String msgText) {
            mSenderId = senderId;
            mReceiverId = receiverId;
            mMsgText = msgText;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            UserConversationModel con = new UserConversationModel();
            try {
                // Simulate network access.
                con = BackendAPI.postMessage(mSenderId,mReceiverId, mMsgText);

            } catch (Exception e) {
                return false;
            }
            // TODO: register the new account here.
            return con.Id > 0;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                etMsg.setText("");
                Toast.makeText(AdDetailActivity.this, "Message sent to agent.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AdDetailActivity.this, "Message sending failed.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public class FavTask extends AsyncTask<Void, Void, Boolean> {

        private final Boolean mMarkFav;

        FavTask(Boolean markFav) {
            mMarkFav = markFav;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Boolean response = false;
            try {
                // Simulate network access.
                response = BackendAPI.markUnmarkFav(Integer.toString(dataModel.ID),userId,mMarkFav);
            } catch (Exception e) {
                response = false;
            }
            // TODO: register the new account here.
            return response;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                renderAd();
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
