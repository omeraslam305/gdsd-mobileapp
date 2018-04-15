package com.example.omer.testapplication.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.omer.testapplication.api.BackendAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class AsyncWebClient {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    private NetworkCallTask mAuthTask = null;
    private ProgressDialog pDialog;
    private Context appContext;

    // constructor
    public AsyncWebClient() {

    }

    private  void showProgress(boolean show, String message){
        if (show) {
            pDialog = new ProgressDialog(appContext);
            pDialog.setMessage(message);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        else{
            pDialog.dismiss();
        }
    }

    public JSONObject makeHttpRequest(String url, String method, Context context,
                                      List<NameValuePair> params) {
        try {
            appContext = context;
            showProgress(true,"Authenticating user. Please waits...");
//            mAuthTask = new NetworkCallTask(params,url,method);
//            jObj = mAuthTask.execute().get();
        } catch (Exception e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON String
        return jObj;
    }

    public class NetworkCallTask extends AsyncTask<Void, Void, JSONObject> {

        private  List<NameValuePair> param;
        private String url, method;

        NetworkCallTask(List<NameValuePair> paramsVal, String urlVal,String methodVal) {
            param = paramsVal;
            url = urlVal;
            method = methodVal;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                // Simulate network access.
                if(method == "POST"){
                    // request method is POST
                    // defaultHttpClient
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(new UrlEncodedFormEntity(param));

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();

                }else if(method == "GET"){
                    // request method is GET
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    String paramString = URLEncodedUtils.format(param, "utf-8");
                    url += "?" + paramString;
                    HttpGet httpGet = new HttpGet(url);

                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
                jObj = new JSONObject(json);
            } catch (Exception e) {
                return null;
            }
            // TODO: register the new account here.
            return jObj;
        }

        @Override
        protected void onPostExecute(final JSONObject success) {
            mAuthTask = null;
            showProgress(false,"");
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
        }
    }
}