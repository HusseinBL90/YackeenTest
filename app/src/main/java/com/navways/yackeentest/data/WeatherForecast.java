package com.navways.yackeentest.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by husse on 25/12/2017.
 */

public class WeatherForecast implements Parcelable{
    private String dt;
    private String temp_min;
    private String temp_max;

    public WeatherForecast(String temp_min, String temp_max, String dt) {
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.dt = dt;
    }

    protected WeatherForecast(Parcel in) {
        dt = in.readString();
        temp_min = in.readString();
        temp_max = in.readString();
    }

    public static final Creator<WeatherForecast> CREATOR = new Creator<WeatherForecast>() {
        @Override
        public WeatherForecast createFromParcel(Parcel in) {
            return new WeatherForecast(in);
        }

        @Override
        public WeatherForecast[] newArray(int size) {
            return new WeatherForecast[size];
        }
    };

    public String getTemp_min() {
        return temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public String getDt() {
        return dt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dt);
        dest.writeString(temp_min);
        dest.writeString(temp_max);
    }
}
