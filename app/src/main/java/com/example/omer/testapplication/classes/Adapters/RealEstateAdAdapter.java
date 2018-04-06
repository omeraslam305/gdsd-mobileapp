package com.example.omer.testapplication.classes.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.omer.testapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RealEstateAdAdapter extends ArrayAdapter<RealEstateAdModel> implements View.OnClickListener {
    private ArrayList<RealEstateAdModel> dataSet;
    Activity mContext;


    // View lookup cache
    private static class ViewHolder {
        TextView txtTitle;
        TextView txtPrice;
        TextView txtArea;
        TextView txtStatus;
        ImageView imgAd;
    }

    public RealEstateAdAdapter(ArrayList<RealEstateAdModel> data, Activity context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        RealEstateAdModel dataModel=(RealEstateAdModel)object;

        switch (v.getId())
        {
//            case R.id.item_info:
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        View rowView;
        try {
            RealEstateAdModel dataModel = getItem(position);
            //LayoutInflater inflater = mContext.getLayoutInflater();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.row_item,null);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.tv_ad_title);
            TextView txtPrice = (TextView) rowView.findViewById(R.id.tv_ad_price);
            TextView txtArea = (TextView) rowView.findViewById(R.id.tv_ad_area);
            TextView txtStatus = (TextView) rowView.findViewById(R.id.tv_ad_status);
            ImageView imgAd = (ImageView) rowView.findViewById(R.id.img_ad);
            txtTitle.setText(dataModel.getTitle());
            txtPrice.setText(dataModel.getPrice());
            txtArea.setText(dataModel.getArea());
            txtStatus.setText(dataModel.getAdStatus());
            String imgPath = "http://ec2-54-200-111-60.us-west-2.compute.amazonaws.com:3000/" + dataModel.getAdImage();
            Picasso.with(getContext()).load(imgPath).fit().into(imgAd);
            return rowView;

        } catch (Exception ex){
            ex.printStackTrace();
            rowView = null;
        }
        return null;
    }
}