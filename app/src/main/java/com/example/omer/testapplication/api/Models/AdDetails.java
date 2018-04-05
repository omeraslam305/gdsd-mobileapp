package com.example.omer.testapplication.api.Models;

import java.util.List;

public class AdDetails {
    public int ID;
    public int AgentId;
    public int BedRooms;
    public int BathRooms;
    public int Kitchen;
    public int LivingRooms;
    public int SquareFeet;
    public int Price;
    public String Address;
    public int Zip;
    public String State;
    public String City;
    public String AdDescription;
    public int Parking;
    public int NumOfFloors;
    public int LotArea;
    public String Title;
    public String Latitude;
    public String Longitude;
    public String createdAt;
    public String updatedAt;
    public int AdStatusId;
    public int AdTypeId;
    public int RealEstateCategoryId;
    public String AgentName;
    public String AgentCompany;
    public String AgentImage;
    public AdType AdType;
    public  AdStatus AdStatus;
    public AdCategory RealEstateCategory;
    public List<UserFavourites> FavouriteAds;
    public List<AdvertisementMedia> AdMedia;
}