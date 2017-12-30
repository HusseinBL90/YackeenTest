package com.navways.yackeentest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navways.yackeentest.data.WeatherForecast;

import java.util.ArrayList;

/**
 * Created by husse on 29/12/2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private Context context;
    private ArrayList<WeatherForecast> forecasts;

    public WeatherAdapter(Context context, ArrayList<WeatherForecast> forecasts) {
        this.context =context;
        this.forecasts = forecasts;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View weatherView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_weather, parent, false);
        WeatherViewHolder weatherViewHolder
                = new WeatherViewHolder(weatherView);
        return weatherViewHolder;

    }
    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.item_tv_weather_min
                    .setText(forecasts.get(position).getTemp_min());
        holder.item_tv_weather_max
                    .setText(forecasts.get(position).getTemp_max());
        holder.item_tv_weather_dt
                    .setText(forecasts.get(position).getDt());
    }
    @Override
    public int getItemCount() {
        return forecasts == null ? 0 : forecasts.size();
    }

    //      Weather ViewHolder
    public class WeatherViewHolder extends RecyclerView.ViewHolder{
        public TextView item_tv_weather_min;
        public TextView item_tv_weather_max;
        public TextView item_tv_weather_dt;
        public WeatherViewHolder(View itemView)
        {
            super(itemView);
            item_tv_weather_min=(TextView)itemView
                    .findViewById(R.id.item_tv_weather_min);
            item_tv_weather_max=(TextView)itemView
                    .findViewById(R.id.item_tv_weather_max);
            item_tv_weather_dt=(TextView)itemView
                    .findViewById(R.id.item_tv_weather_dt);
        }
    }
}