package com.example.omer.testapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.omer.testapplication.api.BackendAPI;
import com.example.omer.testapplication.api.Models.UserConversationModel;
import com.example.omer.testapplication.classes.Adapters.ConversationModel;
import com.example.omer.testapplication.classes.Adapters.UserConvAdapter;
import com.example.omer.testapplication.classes.Adapters.UserMessageAdapter;
import com.example.omer.testapplication.classes.Session;

import java.util.ArrayList;

public class UserMessagesActivity extends AppCompatActivity {
    BackendAPI backendAPI;
    private MessageRenderingTask mAuthTask = null;
    private PostMessageTask mPMTask = null;
    private ProgressDialog pDialog;
    ListView listView;
    ArrayList<UserConversationModel> dataModels;
    private static UserMessageAdapter adapter;
    TextView tvOther;
    String conId, otherName, receiverId, userId;
    ImageButton imgBtnSend;
    EditText etMessageText;
    ArrayList<ConversationModel> conModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messages);

        backendAPI = new BackendAPI();
        pDialog = new ProgressDialog(UserMessagesActivity.this);
        listView=(ListView)findViewById(R.id.messages_view);
        etMessageText = (EditText) findViewById(R.id.et_msg);

        conId = getIntent().getStringExtra("ConId");
        otherName = getIntent().getStringExtra("OtherPerson");
        receiverId = getIntent().getStringExtra("OtherPersonId");
        userId = Integer.toString(Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0)).UserId);

        tvOther = (TextView) findViewById(R.id.tv_other_user);
        tvOther.setText(otherName);



        imgBtnSend = (ImageButton) findViewById(R.id.imgbtn_sendmsg);
        imgBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgText = etMessageText.getText().toString();
                mPMTask = new PostMessageTask(userId,receiverId,msgText);
                mPMTask.execute();
            }
        });

        String userId = Integer.toString(Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0)).UserId);
        mAuthTask = new MessageRenderingTask(conId);
        mAuthTask.execute((Void) null);
    }

    private  void showProgress(boolean show, String message){
        if (show) {
            pDialog.setMessage(message);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        else{
            pDialog.dismiss();
        }
    }

    public class MessageRenderingTask extends AsyncTask<Void, Void, Boolean> {

        private final String mConId;

        MessageRenderingTask(String conId) {
            mConId = conId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true,"Loading Messages. Please wait...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                // Simulate network access.
                BackendAPI backendAPI = new BackendAPI();
                dataModels = backendAPI.getUserMessages(mConId);
            } catch (Exception e) {
                return false;
            }
            // TODO: register the new account here.
            return dataModels != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false,"");
            if (success) {
                renderList();
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public void renderList(){
        conModel = new ArrayList<ConversationModel>();
        int userId = Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0)).UserId;
        for (UserConversationModel con : dataModels){
            conModel.add(new ConversationModel(con.Id,con.ConversationID,con.SenderID,con.ReceiverID,con.MsgDate,con.MessageText,con.ReceiverName,con.SendererName, con.SenderName,con.SenderImage,con.ReceiverImage,0));
        }
        adapter= new UserMessageAdapter(conModel,UserMessagesActivity.this,userId);
        listView.setAdapter(adapter);
        listView.setSelection(listView.getAdapter().getCount()-1);
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
            showProgress(true,"Sending Message. Please wait...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                // Simulate network access.
                BackendAPI backendAPI = new BackendAPI();
                UserConversationModel con = backendAPI.postMessage(mSenderId,mReceiverId, mMsgText);
                if(con != null){
                    conModel.add(new ConversationModel(con.Id,con.ConversationID,con.SenderID,con.ReceiverID,con.MsgDate,con.MessageText,con.ReceiverName,con.SendererName, con.SenderName,con.SenderImage,con.ReceiverImage,0));
                    new ReceiverThread().run();
                }
            } catch (Exception e) {
                return false;
            }
            // TODO: register the new account here.
            return dataModels != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false,"");
            if (success) {
                etMessageText.setText("");
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    private class ReceiverThread extends Thread {
        @Override
        public void run() {
            UserMessagesActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                adapter.notifyDataSetChanged();
                listView.setSelection(listView.getAdapter().getCount()-1);
                }
            });
        }
    }
}
