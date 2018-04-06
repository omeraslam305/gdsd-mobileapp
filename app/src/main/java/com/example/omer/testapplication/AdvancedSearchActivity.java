package com.example.omer.testapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AdvancedSearchActivity extends AppCompatActivity {
    EditText etKeyword,etSqft;
    Spinner spAccomodation, spBedrooms, spAdType;
    Button btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        etKeyword = (EditText) findViewById(R.id.et_Keyword);
        etSqft = (EditText) findViewById(R.id.et_Sqft);

        spAccomodation = (Spinner) findViewById(R.id.sp_Accomodation);
        spBedrooms = (Spinner) findViewById(R.id.sp_Bedrooms);
        spAdType = (Spinner) findViewById(R.id.sp_AdType);

        btnSearch = (Button) findViewById(R.id.btn_Search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAd();
            }
        });
    }

    public void searchAd(){
        String keyword = etKeyword.getText().toString();
        String accomodation = spAccomodation.getSelectedItem().toString().equals("Flat") ? "2" : "1";
        String bedrooms = spBedrooms.getSelectedItem().toString();
        String sqft = etSqft.getText().toString();
        String adType = spAdType.getSelectedItem().toString().equals("Rental") ? "1" : "2";

        Intent intent = new Intent(AdvancedSearchActivity.this,DashboardActivity.class);
        intent.putExtra("navigationFrom","AS");
        intent.putExtra("keyword",keyword);
        intent.putExtra("accomodation",accomodation);
        intent.putExtra("bedrooms",bedrooms);
        intent.putExtra("sqft",sqft);
        intent.putExtra("adType",adType);
        startActivity(intent);

    }
}
