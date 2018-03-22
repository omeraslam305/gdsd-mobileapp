package com.example.omer.testapplication.classes.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.omer.testapplication.R;

import java.util.ArrayList;

public class UserMessageAdapter extends BaseAdapter {
    private ArrayList<ConversationModel> dataSet;
    Activity mContext;
    int mUserId;

    public UserMessageAdapter(ArrayList<ConversationModel> data, Activity context, int userId) {
        //super(context, R.layout.conversation_item, data);
        this.dataSet = data;
        this.mContext=context;
        this.mUserId = userId;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return dataSet.get(i);
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    public void updateReceiptsList(ArrayList<ConversationModel> newlist) {
        dataSet.clear();
        dataSet.addAll(newlist);
        this.notifyDataSetChanged();
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        View rowView;
        try {
            ConversationModel dataModel = dataSet.get(position);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            //current user
            if(dataModel.getSenderID() == mUserId){
                rowView = inflater.inflate(R.layout.my_message,null);
                TextView txtMsgText = (TextView) rowView.findViewById(R.id.message_body);
                txtMsgText.setText(dataModel.getMessageText());
                return rowView;
            }else{
                rowView = inflater.inflate(R.layout.their_message,null);
                TextView txtName = (TextView) rowView.findViewById(R.id.name);
                TextView txtMsgText = (TextView) rowView.findViewById(R.id.message_body);
                txtName.setText(mUserId ==  dataModel.getSenderID() ? dataModel.getReceiverName() : dataModel.getSenderName());
                txtMsgText.setText(dataModel.getMessageText());
                return rowView;
            }

        } catch (Exception ex){
            ex.printStackTrace();
            rowView = null;
        }
        return null;
    }
}