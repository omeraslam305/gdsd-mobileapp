package com.example.omer.testapplication.api;

import android.content.Context;
import android.util.Log;

import com.example.omer.testapplication.api.Models.AdDetails;
import com.example.omer.testapplication.api.Models.RealEstateAd;
import com.example.omer.testapplication.api.Models.User;
import com.example.omer.testapplication.api.Models.UserConversationModel;
import com.example.omer.testapplication.util.AsyncWebClient;
import com.example.omer.testapplication.util.WebClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BackendAPI {
    private static WebClient jParser = new WebClient();
    public static String baseAddress = "http://ec2-54-200-111-60.us-west-2.compute.amazonaws.com:3000/api/";
    //public static String baseAddress = "http://192.168.159.1:3000/api/";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_DATA = "data";

    public static User authenticateUser(String userName, String password){
        User userObj = new User();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("username", userName));
            nameValuePairs.add(new BasicNameValuePair("password", password));

            String url = baseAddress + "login";
            JSONObject jsonObj = jParser.makeHttpRequest(url, "POST", nameValuePairs);
            Log.d("User object: ", jsonObj.toString());

            if (jsonObj != null){
                if(Boolean.parseBoolean(jsonObj.getString(TAG_SUCCESS))){
                    JSONObject dataObj = jsonObj.getJSONObject(TAG_DATA);
                    JsonElement mJson =  new JsonParser().parse(dataObj.toString());
                    userObj = new Gson().fromJson(mJson, User.class);
                } else
                    userObj = null;
            }
        } catch (Exception ex){
            Log.d("Exception", ex.getMessage());
        }
        return  userObj;
    }

    public static User registerUser(String fname, String lname ,String email, String phone, String username, String password, String cpassword){
        User userObj = new User();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("fname", fname));
            nameValuePairs.add(new BasicNameValuePair("lname", lname));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("phone", phone));
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("cpassword", cpassword));
            nameValuePairs.add(new BasicNameValuePair("password", password));

            String url = baseAddress + "signup";
            JSONObject jsonObj = jParser.makeHttpRequest(url, "POST", nameValuePairs);
            Log.d("User object: ", jsonObj.toString());

            if (jsonObj != null){
                if(Boolean.parseBoolean(jsonObj.getString(TAG_SUCCESS))){
                    JSONObject dataObj = jsonObj.getJSONObject(TAG_DATA);
                    JsonElement mJson =  new JsonParser().parse(dataObj.toString());
                    userObj = new Gson().fromJson(mJson, User.class);
                }else
                    userObj = null;
            }
        } catch (Exception ex){
            Log.d("Exception", ex.getMessage());
        }
        return  userObj;
    }

    public static ArrayList<RealEstateAd> getRealEstateList(String searchText, String typeOfAccomodation, String noOfBedRooms, String squareFeet, String adType){
        ArrayList<RealEstateAd> adList = new ArrayList<RealEstateAd>();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("searchText", searchText));
            nameValuePairs.add(new BasicNameValuePair("typeOfAccomodation", typeOfAccomodation));
            nameValuePairs.add(new BasicNameValuePair("noOfBedRooms", noOfBedRooms));
            nameValuePairs.add(new BasicNameValuePair("squareFeet", squareFeet));
            nameValuePairs.add(new BasicNameValuePair("adType", adType));

            String url = baseAddress + "listings";
            JSONObject jsonObj = jParser.makeHttpRequest(url, "POST", nameValuePairs);
            Log.d("Ad object: ", jsonObj.toString());

            if (jsonObj != null){
                if(Boolean.parseBoolean(jsonObj.getString(TAG_SUCCESS))){
                    if(jsonObj.getJSONArray("data").length() > 0){
                        JSONArray dataArray = jsonObj.getJSONArray(TAG_DATA);
                        Type listType = new TypeToken<ArrayList<RealEstateAd>>(){}.getType();
                        adList = (ArrayList<RealEstateAd>) new Gson().fromJson(dataArray.toString(), listType);
                    }
                }
            }
        } catch (Exception ex){
                Log.d("Exception", ex.getMessage());
        }
        return  adList;
    }

    public static ArrayList<RealEstateAd> getFavouriteAds(String userId, String userType){
        ArrayList<RealEstateAd> adList = new ArrayList<RealEstateAd>();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userId", userId));
            nameValuePairs.add(new BasicNameValuePair("userType", userType));

            String url = baseAddress + "listings/user";
            JSONObject jsonObj = jParser.makeHttpRequest(url, "POST", nameValuePairs);
            Log.d("Ad object: ", jsonObj.toString());

            if (jsonObj != null){
                if(Boolean.parseBoolean(jsonObj.getString(TAG_SUCCESS))){
                    if(jsonObj.getJSONArray("data").length() > 0){
                        JSONArray dataArray = jsonObj.getJSONArray(TAG_DATA);
                        Type listType = new TypeToken<ArrayList<RealEstateAd>>(){}.getType();
                        adList = (ArrayList<RealEstateAd>) new Gson().fromJson(dataArray.toString(), listType);
                    }
                }
            }
        } catch (Exception ex){
            Log.d("Exception", ex.getMessage());
        }
        return  adList;
    }

    public static User updateProfile(String userId, String fname, String lname ,String email, String phone, String address){
        User userObj = new User();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userId", userId));
            nameValuePairs.add(new BasicNameValuePair("fname", fname));
            nameValuePairs.add(new BasicNameValuePair("lname", lname));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("mobile", phone));
            nameValuePairs.add(new BasicNameValuePair("address", address));
            nameValuePairs.add(new BasicNameValuePair("userImage", null));

            String url = baseAddress + "user/update";
            JSONObject jsonObj = jParser.makeHttpRequest(url, "POST", nameValuePairs);
            Log.d("User object: ", jsonObj.toString());

            if (jsonObj != null){
                if(Boolean.parseBoolean(jsonObj.getString(TAG_SUCCESS))){
                    JSONObject dataObj = jsonObj.getJSONObject(TAG_DATA);
                    JsonElement mJson =  new JsonParser().parse(dataObj.toString());
                    userObj = new Gson().fromJson(mJson, User.class);
                }
            }
        } catch (Exception ex){
            Log.d("Exception", ex.getMessage());
        }
        return  userObj;
    }

    public static ArrayList<UserConversationModel> getUserConversation(String receiverID){
        ArrayList<UserConversationModel> conList = new ArrayList<UserConversationModel>();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("receiverID", receiverID));

            String url = baseAddress + "GetAllConversations";
            JSONObject jsonObj = jParser.makeHttpRequest(url, "GET", nameValuePairs);
            Log.d("User object: ", jsonObj.toString());

            if (jsonObj != null){
                if(Boolean.parseBoolean(jsonObj.getString(TAG_SUCCESS))){
                    JSONArray dataArray = jsonObj.getJSONArray(TAG_DATA);
                    Type listType = new TypeToken<ArrayList<UserConversationModel>>(){}.getType();
                    conList = (ArrayList<UserConversationModel>) new Gson().fromJson(dataArray.toString(), listType);
                }
            }
        } catch (Exception ex){
            Log.d("Exception", ex.getMessage());
        }
        return  conList;
    }

    public static ArrayList<UserConversationModel> getUserMessages(String conversationId){
        ArrayList<UserConversationModel> conList = new ArrayList<UserConversationModel>();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("conversationID", conversationId));

            String url = baseAddress + "GetAllMessages";
            JSONObject jsonObj = jParser.makeHttpRequest(url, "GET", nameValuePairs);
            Log.d("User object: ", jsonObj.toString());

            if (jsonObj != null){
                if(Boolean.parseBoolean(jsonObj.getString(TAG_SUCCESS))){
                    JSONArray dataArray = jsonObj.getJSONArray(TAG_DATA);
                    Type listType = new TypeToken<ArrayList<UserConversationModel>>(){}.getType();
                    conList = (ArrayList<UserConversationModel>) new Gson().fromJson(dataArray.toString(), listType);
                }
            }
        } catch (Exception ex){
            Log.d("Exception", ex.getMessage());
        }
        return  conList;
    }

    public static UserConversationModel postMessage(String senderId, String receiverId, String msgText){
        UserConversationModel msgObj = new UserConversationModel();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("senderId", senderId));
            nameValuePairs.add(new BasicNameValuePair("receiverId", receiverId));
            nameValuePairs.add(new BasicNameValuePair("msgTxt", msgText));

            String url = baseAddress + "PostMessage";
            JSONObject jsonObj = jParser.makeHttpRequest(url, "POST", nameValuePairs);
            Log.d("Message object: ", jsonObj.toString());

            if (jsonObj != null){
                if(Boolean.parseBoolean(jsonObj.getString(TAG_SUCCESS))){
                    JSONObject dataObj = jsonObj.getJSONObject(TAG_DATA);
                    JsonElement mJson =  new JsonParser().parse(dataObj.toString());
                    msgObj = new Gson().fromJson(mJson, UserConversationModel.class);
                }
            }
        } catch (Exception ex){
            Log.d("Exception", ex.getMessage());
        }
        return  msgObj;
    }

    public static AdDetails getAdDetail(String listingId){
        AdDetails adObj = new AdDetails();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("listing_id", listingId));

            String url = baseAddress + "listing";
            JSONObject jsonObj = jParser.makeHttpRequest(url, "GET", nameValuePairs);
            Log.d("Ad object: ", jsonObj.toString());

            if (jsonObj != null){
                if(Boolean.parseBoolean(jsonObj.getString(TAG_SUCCESS))){
                    JSONObject dataObj = jsonObj.getJSONObject(TAG_DATA);
                    JsonElement mJson =  new JsonParser().parse(dataObj.toString());
                    adObj = new Gson().fromJson(mJson, AdDetails.class);
                }
            }
        } catch (Exception ex){
            Log.d("Exception", ex.getMessage());
        }
        return  adObj;
    }

    public static Boolean markUnmarkFav(String adId, String userID, String status){
        Boolean response = false;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("AdID", adId));
            nameValuePairs.add(new BasicNameValuePair("UserID", userID));
            nameValuePairs.add(new BasicNameValuePair("Status", status));

            String url = baseAddress + "favoriteListing";
            JSONObject jsonObj = jParser.makeHttpRequest(url, "POST", nameValuePairs);
            Log.d("Message object: ", jsonObj.toString());

            if (jsonObj != null){
                response = Boolean.parseBoolean(jsonObj.getString(TAG_SUCCESS));
            }
        } catch (Exception ex){
            Log.d("Exception", ex.getMessage());
        }
        return  response;
    }

}