package com.example.omer.testapplication.api.Models;

import java.util.List;



public class RealEstateAd {
    public int Id;
    public String Title;
    public String AdDescription;
    public String Price;
    public String City;
    public String State;
    public String Address;
    public String Latitude;
    public String Longitude;
    public int BedRooms;
    public int Zip;
    public int AdStatusId;
//    public String AdStatus;
    public List<AdvertisementMedia> AdMedia;
}