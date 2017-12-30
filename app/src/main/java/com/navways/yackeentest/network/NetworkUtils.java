package com.navways.yackeentest.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by husse on 26/12/2017.
 */

public class NetworkUtils {
    
    private static final String NYT_KEY="YOUR KEY";
    private static final String OPEN_WEATHER_KEY="YOUR KEY";

    public static boolean isInternetConnected(Context context){
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null &&
                networkInfo.isConnected();
    }
    // Stories
    private static final String NY_BASE_URL
            = "https://api.nytimes.com/svc/topstories/v2/home.json?api-key="+NYT_KEY;
    public static StringBuilder storiesJsonStringBuilder(){
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        StringBuilder jsonStringBuilder = null;
        URL url = null;
        try {
            url = new URL(NY_BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            jsonStringBuilder = new StringBuilder();
            String responseLine;
            while( (responseLine=bufferedReader.readLine()) !=null )
            {
                jsonStringBuilder.append(responseLine);
            }
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        return jsonStringBuilder;
    }
    // Weather Forecast
    private static final String WEATHER_BASE_URL
            = "http://samples.openweathermap.org/data/2.5/forecast?lat=30.105442&lon=31.328697&appid="+OPEN_WEATHER_KEY;
    public static StringBuilder forecastJsonStringBuilder(){
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        StringBuilder jsonStringBuilder = null;
        URL url = null;
        try {
            url = new URL(WEATHER_BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            jsonStringBuilder = new StringBuilder();
            String responseLine;
            while( (responseLine=bufferedReader.readLine()) !=null )
            {
                jsonStringBuilder.append(responseLine);
            }
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        return jsonStringBuilder;
    }

}
