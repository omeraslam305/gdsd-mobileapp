package com.example.omer.testapplication.classes.Adapters;

public class RealEstateAdModel {
    public int Id;
    public String Title;
    public String Price;
    public String Area;
    public String AdStatus;
    public String AdImage;

    public RealEstateAdModel(int Id, String Title, String Price, String Area, String AdStatus, String AdImage ) {
        this.Id = Id;
        this.Title = Title;
        this.Price = Price;
        this.Area = Area;
        this.AdStatus = AdStatus;
        this.AdImage = AdImage;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getAdStatus() {
        return AdStatus;
    }

    public void setAdStatus(String adStatus) {
        AdStatus = adStatus;
    }

    public String getAdImage() {
        return AdImage;
    }

    public void setAdImage(String adImage) {
        AdImage = adImage;
    }
}