package com.navways.yackeentest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.navways.yackeentest.data.TopStories;
import com.navways.yackeentest.data.WeatherForecast;
import com.navways.yackeentest.data.YacheenDBHelper;
import com.navways.yackeentest.network.NetworkUtils;
import com.navways.yackeentest.network.OpenJsonData;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<TopStories>> {

    private ImageView iv_bg;
    private RecyclerView rv;
    private TextView tv_error_msg;
    private ProgressBar pb_loading;

    private static final int LOADER_ID = 0;
    private MainAdapter mAdapter;
    private LoaderManager.LoaderCallbacks<ArrayList<TopStories>> callbacks;
    private Bundle bundleForLoader;
    private ArrayList<TopStories> stories = null;
    private ArrayList<WeatherForecast> forecasts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_bg = (ImageView) findViewById(R.id.iv_bg);
        rv = (RecyclerView) findViewById(R.id.rv);
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false));
        mAdapter =
                new MainAdapter(this);
        callbacks = MainActivity.this;
        bundleForLoader = null;

        if (savedInstanceState == null)
            loadData();
    }

    private void loadData() {
        if (NetworkUtils.isInternetConnected(this)) {
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<ArrayList<TopStories>> loader
                    = loaderManager.getLoader(LOADER_ID);
            if (loader == null) {
                loaderManager.initLoader(
                        LOADER_ID, bundleForLoader, callbacks);
            } else {
                loaderManager.restartLoader
                        (LOADER_ID, bundleForLoader, callbacks);
            }
        } else if (!NetworkUtils.isInternetConnected(getApplicationContext())) {
            selectDb();
            if (!stories.isEmpty()) {
                showDataView();
                mAdapter.setStories(stories);
                rv.setAdapter(mAdapter);
            } else {
                showErrorMessage();
            }
        } else {
            showErrorMessage();
        }
    }

    // Loader
    @Override
    public Loader<ArrayList<TopStories>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<TopStories>>(this) {
            @Override
            protected void onStartLoading() {
                pb_loading.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public ArrayList<TopStories> loadInBackground() {
                stories = new ArrayList<>();
                StringBuilder jsonStories = NetworkUtils.storiesJsonStringBuilder();
                StringBuilder jsonforecasts = NetworkUtils.forecastJsonStringBuilder();
                try {
                    if (jsonStories.length() > 0) {
                        stories = OpenJsonData.getStories(jsonStories);
                    }
                    if (jsonforecasts.length() > 0) {
                        forecasts = OpenJsonData.getWeatherData(jsonforecasts);
                        mAdapter.setForcasts(forecasts);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                return stories;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<TopStories>> loader,
                               ArrayList<TopStories> data) {
        pb_loading.setVisibility(View.INVISIBLE);
        if (data != null) {
            showDataView();
            mAdapter.setStories(stories);
            rv.setAdapter(mAdapter);
            updateDb(data,forecasts);
        } else {
            showErrorMessage();
        }
    }

    private void selectDb() {
        Cursor c;
        stories = new ArrayList<>();
        c = getContentResolver().query(YacheenDBHelper.CONTENT_URI_STORIES,
                YacheenDBHelper.STORIES_COLUMNS,
                null,
                null,
                null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                TopStories story = new TopStories(
                        c.getString(0),
                        c.getString(1),
                        c.getString(2), null);
                stories.add(story);
                c.moveToNext();
            }
        }
        forecasts = new ArrayList<>();
        c = getContentResolver().query(YacheenDBHelper.CONTENT_URI_WEATHER,
                YacheenDBHelper.WEATHER_COLUMNS,
                null,
                null,
                null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                WeatherForecast w = new WeatherForecast(
                        c.getString(1),
                        c.getString(3),
                        c.getString(0));
                forecasts.add(w);
                c.moveToNext();
            }
        }
        c.close();
    }

    private void updateDb(ArrayList<TopStories> tStories,
                          ArrayList<WeatherForecast> wForecasts) {
        Uri uri;
        ContentValues cvStories, cvWeatherForecasts;
        uri = YacheenDBHelper.CONTENT_URI_STORIES;
        getContentResolver().delete(uri, null, null);
        for (TopStories s : tStories) {
            cvStories = new ContentValues();
            cvStories.put(YacheenDBHelper.COLUMN_STORIES_TITLE, s.getTitle());
            cvStories.put(YacheenDBHelper.COLUMN_STORIES_DATE, s.getPublished_date());
            cvStories.put(YacheenDBHelper.COLUMN_STORIES_IMG_URL, s.getImgUrl());
            getContentResolver().insert(uri, cvStories);
        }
        uri = YacheenDBHelper.CONTENT_URI_WEATHER;
        getContentResolver().delete(uri, null, null);
        for (WeatherForecast w : wForecasts) {
            cvStories = new ContentValues();
            cvStories.put(YacheenDBHelper.COLUMN_WEATHER_DATE, w.getDt());
            cvStories.put(YacheenDBHelper.COLUMN_WEATHER_TEMP_MIN, w.getTemp_min());
            cvStories.put(YacheenDBHelper.COLUMN_WEATHER_TEMP_MAX, w.getTemp_max());
            getContentResolver().insert(uri, cvStories);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<TopStories>> loader) {
    }

    private void showDataView() {
        tv_error_msg.setVisibility(View.INVISIBLE);
        rv.setVisibility(View.VISIBLE);
        AlphaAnimation fadeAnim = new AlphaAnimation(1.0f, 0.1f);
        fadeAnim.setDuration(2000);
        fadeAnim.setFillAfter(true);
        iv_bg.startAnimation(fadeAnim);
    }

    private void showErrorMessage() {
        tv_error_msg.setVisibility(View.VISIBLE);
        rv.setVisibility(View.INVISIBLE);
        AlphaAnimation fadeAnim = new AlphaAnimation(0.1f, 1.0f);
        fadeAnim.setDuration(2000);
        fadeAnim.setFillAfter(true);
        iv_bg.startAnimation(fadeAnim);
    }

    private static final String
            KEY_LIST_STATE = "key_list_state",
            KEY_STORIES_DATA = "key_stories",
            KEY_WEATHER_DATA = "key_forecasts";
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_STORIES_DATA, stories);
        outState.putParcelableArrayList(KEY_WEATHER_DATA, forecasts);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        showDataView();
        stories = savedInstanceState.getParcelableArrayList(KEY_STORIES_DATA);
        forecasts = savedInstanceState.getParcelableArrayList(KEY_WEATHER_DATA);
        mAdapter.setStories(stories);
        mAdapter.setForcasts(forecasts);
        Toast.makeText(this,"Stories : " + stories.size()+
                        " - Forecasts : " + forecasts.size(),
                Toast.LENGTH_LONG).show();
        rv.setAdapter(mAdapter);
    }

}
