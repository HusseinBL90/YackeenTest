package com.navways.yackeentest.network;
import com.navways.yackeentest.data.TopStories;
import com.navways.yackeentest.data.WeatherForecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by husse on 26/12/2017.
 */

public class OpenJsonData {

    private static final String TYPE_STORY="ssss", TYPE_WEATHER="wwww";
    // NY Stories
    private static final String STORY_TITLE = "title";
    private static final String STORY_P_DATE= "published_date";
    private static final String STORY_ARRAY_MULTIMEDIA  = "multimedia";
    private static final String STORY_IMG_URL= "url";
    // Weather
    private static final String WEATHER_TEMP_MIN = "temp_min";
    private static final String WEATHER_TEMP_MAX = "temp_max";

    private static final String WEATHER_DATE = "dt";
    public static ArrayList<TopStories> getStories
            (StringBuilder jsonStringBuilder)
            throws JSONException {
        ArrayList<TopStories> Stories = new ArrayList<>();
        JSONObject resultObject = new JSONObject(jsonStringBuilder.toString());
        JSONArray storiesArray = resultObject.getJSONArray("results");
        int NUM_ITEMS  = storiesArray.length();
        for (int i = 0 ; i < NUM_ITEMS ; i++) {
            JSONObject sObject = storiesArray.getJSONObject(i);
            String storyTitle = sObject.getString(STORY_TITLE);
            String storyDate = sObject.getString(STORY_P_DATE);
            storyDate = formatedDT(storyDate, TYPE_STORY);
            JSONArray multimediaArray = sObject.getJSONArray(STORY_ARRAY_MULTIMEDIA);
            String storyImgUrl = "";
            if (multimediaArray.length()!=0){
                JSONObject multimediaObject = multimediaArray.getJSONObject(0);
                storyImgUrl = multimediaObject.getString(STORY_IMG_URL);
            }
            TopStories story = new TopStories(
                    storyTitle,
                    storyDate,
                    storyImgUrl,
                    null
            );
            Stories.add(story);
        }
        return Stories;
    }

    public static ArrayList<WeatherForecast> getWeatherData
                (StringBuilder jsonStringBuilder)
            throws JSONException {
        ArrayList<WeatherForecast> forecasts = new ArrayList<>();
        JSONObject resultObject = new JSONObject(jsonStringBuilder.toString());
        JSONArray forecastsArray = resultObject.getJSONArray("list");
        int NUM_ITEMS  = forecastsArray.length();
        for (int i = 0 ; i < NUM_ITEMS ; i++) {
            JSONObject wObject = forecastsArray.getJSONObject(i);
            String dt = wObject.getString(WEATHER_DATE);
            dt = formatedDT(dt, TYPE_STORY);
            JSONObject mainObject = wObject.getJSONObject("main");
            String tempMin = mainObject.getString(WEATHER_TEMP_MIN);
            String tempMax = mainObject.getString(WEATHER_TEMP_MAX);

            WeatherForecast forecast = new WeatherForecast(
                    toCelsius(tempMin),
                    toCelsius(tempMax),
                    dt
            );
            forecasts.add(forecast);
        }
        return forecasts;
    }
    private static String toCelsius(String f){
        String cTxt;
        try {
            double dVal = Double.valueOf(f)-32;
            BigDecimal cRounded = new BigDecimal(dVal)
                    .divide(new BigDecimal(1.8),0, RoundingMode.HALF_UP);
            cTxt = String.valueOf(cRounded).concat(" °C");
        }catch (NumberFormatException e){
            cTxt = f.concat(" °F");
        }
        return cTxt;
    }
    // NY Ex: 2017-12-26T18:13:03-05:00
    // Weather: 1485799200
    // Pattern required: Monday 23 July.
    private static String formatedDT(String dt, String type){
        String formatedDate = "";
        SimpleDateFormat inFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-Z");
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE dd MMMM");
        try {
            switch (type){
                case TYPE_STORY:
                    formatedDate = inFormat.format(outFormat.parse(dt));
                    break;
                case TYPE_WEATHER:
                    Date date = new Date(Long.valueOf(dt));
                    formatedDate = outFormat.parse(date.toString()).toString();
                    break;
            }

        }catch (ParseException e){
            formatedDate = dt;
        }
        return formatedDate;
    }
}
