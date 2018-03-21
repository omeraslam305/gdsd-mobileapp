package com.example.omer.testapplication.classes.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.omer.testapplication.R;
import com.example.omer.testapplication.api.Models.UserConversationModel;
import com.example.omer.testapplication.classes.Session;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserConvAdapter extends ArrayAdapter<ConversationModel> implements View.OnClickListener {
    private ArrayList<ConversationModel> dataSet;
    Activity mContext;
    int mUserId;

    public UserConvAdapter(ArrayList<ConversationModel> data, Activity context, int userId) {
        super(context, R.layout.conversation_item, data);
        this.dataSet = data;
        this.mContext=context;
        this.mUserId = userId;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        ConversationModel dataModel=(ConversationModel)object;

        switch (v.getId())
        {

        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        View rowView;
        try {
            ConversationModel dataModel = getItem(position);
            //LayoutInflater inflater = mContext.getLayoutInflater();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.conversation_item,null);

            TextView txtConvId = (TextView) rowView.findViewById(R.id.tv_conversation_id);
            TextView txtUserId = (TextView) rowView.findViewById(R.id.tv_user_id);
            TextView txtName = (TextView) rowView.findViewById(R.id.tv_name);
            TextView txtMsgDate = (TextView) rowView.findViewById(R.id.tv_msg_date);
            TextView txtMsgText = (TextView) rowView.findViewById(R.id.tv_msg_text);
            txtMsgDate.setText(dataModel.getMsgDate());
            txtName.setText(mUserId ==  dataModel.getSenderID() ? dataModel.getReceiverName() : dataModel.getSendererName());
            txtUserId.setText(mUserId ==  dataModel.getSenderID() ? Integer.toString(dataModel.getReceiverID()) : Integer.toString(dataModel.getSenderID()));
            txtMsgText.setText(dataModel.getMessageText());
            txtConvId.setText(Integer.toString(dataModel.getConversationID()));
            return rowView;

        } catch (Exception ex){
            ex.printStackTrace();
            rowView = null;
        }
        return null;
    }
}