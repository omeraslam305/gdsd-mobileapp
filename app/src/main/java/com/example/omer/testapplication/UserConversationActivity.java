package com.example.omer.testapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.omer.testapplication.api.BackendAPI;
import com.example.omer.testapplication.api.Models.RealEstateAd;
import com.example.omer.testapplication.api.Models.UserConversationModel;
import com.example.omer.testapplication.classes.Adapters.ConversationModel;
import com.example.omer.testapplication.classes.Adapters.RealEstateAdAdapter;
import com.example.omer.testapplication.classes.Adapters.RealEstateAdModel;
import com.example.omer.testapplication.classes.Adapters.UserConvAdapter;
import com.example.omer.testapplication.classes.Session;

import java.util.ArrayList;

public class UserConversationActivity extends AppCompatActivity {
    BackendAPI backendAPI;
    private ConversationRenderingTask mAuthTask = null;
    private ProgressDialog pDialog;
    ListView listView;
    ArrayList<UserConversationModel> dataModels;
    String userId;
    private static UserConvAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_conversation);

        backendAPI = new BackendAPI();
        pDialog = new ProgressDialog(UserConversationActivity.this);
        listView=(ListView)findViewById(R.id.list_conversation);

        userId = Integer.toString(Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0)).UserId);
        mAuthTask = new ConversationRenderingTask(userId);
        mAuthTask.execute((Void) null);
    }

    private  void showProgress(boolean show){
        if (show) {
            pDialog.setMessage("Loading Messages. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        else{
            pDialog.dismiss();
        }
    }

    public class ConversationRenderingTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserId;

        ConversationRenderingTask(String userId) {
            mUserId = userId;
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
                dataModels = backendAPI.getUserConversation(mUserId);
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
        ArrayList<ConversationModel> conModel = new ArrayList<ConversationModel>();
        for (UserConversationModel con : dataModels){
            conModel.add(new ConversationModel(con.Id,con.ConversationID,con.SenderID,con.ReceiverID,con.MsgDate,con.MessageText,con.ReceiverName,con.SendererName, con.SenderName,con.SenderImage,con.ReceiverImage,0));
        }
        adapter= new UserConvAdapter(conModel,UserConversationActivity.this,Integer.parseInt(userId));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ConversationModel obj = (ConversationModel)parent.getAdapter().getItem(position);
                Intent intent = new Intent(UserConversationActivity.this,UserMessagesActivity.class);
                intent.putExtra("ConId", Integer.toString(obj.getConversationID()));
                String name = Integer.parseInt(userId) ==  obj.getSenderID() ? obj.getReceiverName() : obj.getSendererName();
                int senderId = Integer.parseInt(userId) ==  obj.getSenderID() ? obj.getReceiverID() : obj.getSenderID();
                intent.putExtra("OtherPerson", name);
                intent.putExtra("OtherPersonId", Integer.toString(senderId));
                startActivity(intent);
            }
        });

    }
}
