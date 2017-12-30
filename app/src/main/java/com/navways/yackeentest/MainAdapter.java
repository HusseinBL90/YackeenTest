package com.navways.yackeentest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.navways.yackeentest.data.TopStories;
import com.navways.yackeentest.data.WeatherForecast;
import com.navways.yackeentest.network.NetworkUtils;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
/**
 * Created by husse on 26/12/2017.
 */

public class MainAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private static ArrayList<TopStories> stories;
    private static ArrayList<WeatherForecast> forcasts;
    private static final int TYPE_STORY = 111, TYPE_WEATHER = 222;

    public MainAdapter(Context context){
        this.context = context;
    }
    public void setStories(ArrayList<TopStories> stories){
        this.stories = stories;
    }
    public void setForcasts(ArrayList<WeatherForecast> forcasts) {
        this.forcasts = forcasts;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = 0;
        RecyclerView.ViewHolder viewHolder;
        switch (viewType){
            case TYPE_STORY:
                layout=R.layout.item_story;
                View storyView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(layout, parent, false);
                viewHolder = new StoryViewHolder(storyView);
                break;
            case TYPE_WEATHER:
                layout=R.layout.item_rv;
                View weatherView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(layout, parent, false);
                viewHolder=new WeatherContainerViewHolder(weatherView);
                break;
            default:
                viewHolder=null;
                break;
        }
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType=holder.getItemViewType();
        switch (viewType){
            case TYPE_STORY:
                if (storyIndx < stories.size()){
                    String imgUrl = stories.get(position).getImgUrl();
                    if (!imgUrl.isEmpty()& NetworkUtils.isInternetConnected(context)) {
                        Picasso.with(context)
                                .load(imgUrl)
                                .resize(72,72)
                                .centerCrop()
                                .into(((StoryViewHolder)holder).iv_story);
                    }
                    ((StoryViewHolder)holder).tv_story_title
                            .setText(stories.get(storyIndx).getTitle());
                    ((StoryViewHolder)holder).tv_story_date
                            .setText(stories.get(storyIndx).getPublished_date());
                    storyIndx++;
                }
                break;
            case TYPE_WEATHER:
                if (weatherIndx < forcasts.size()){
                    ((WeatherContainerViewHolder)holder).item_rv_horizontal
                            .setHasFixedSize(true);
                    ((WeatherContainerViewHolder)holder).item_rv_horizontal
                            .setLayoutManager(new LinearLayoutManager(
                            context,
                            LinearLayoutManager.HORIZONTAL,
                            false));
                    ArrayList<WeatherForecast> fiveItems = new ArrayList();
                    for (int i=weatherIndx ; i<weatherIndx+5; i++){
                        fiveItems.add(forcasts.get(i));
                    }
                    WeatherAdapter mAdapter=
                            new WeatherAdapter(context,fiveItems);
                    ((WeatherContainerViewHolder)holder).item_rv_horizontal
                            .setAdapter(mAdapter);
                    weatherIndx = weatherIndx+5;
                    if (fiveItems.isEmpty())
                        holder = null;
                }
                break;
        }
    }
    @Override
    public int getItemCount() {
        return stories == null ? 0 : stories.size();
    }
    private static int
            storyIndx = 0, weatherIndx = 0;
    @Override
    public int getItemViewType(int position) {
        int s = stories.size(), f = forcasts.size();
        if( s>=5 & f>=5 ){
            if (position%5>0 | position<5){
                return TYPE_STORY;
            }else if(position%5==0){
                return TYPE_WEATHER;
            }else {
                return -5;
            }
        }else if(s > 0 & forcasts.isEmpty()){
            return TYPE_STORY;
        }else if(stories.isEmpty() & f > 0){
            return TYPE_WEATHER;
        }else{
            return -5;
        }
    }

    //      Story ViewHolder
    public class StoryViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_story_title;
        public TextView tv_story_date;
        public ImageView iv_story;
        public StoryViewHolder(View itemView)
        {
            super(itemView);
            tv_story_title=(TextView)itemView.findViewById(R.id.item_tv_story_title);
            tv_story_date=(TextView)itemView.findViewById(R.id.item_tv_story_dt);
            iv_story =(ImageView) itemView.findViewById(R.id.item_iv_story);
        }
    }
    //     RecyclerView Weather ViewHolder
    public class WeatherContainerViewHolder extends RecyclerView.ViewHolder{
        public RecyclerView item_rv_horizontal;
        public WeatherContainerViewHolder
                (View itemView){
            super(itemView);
            item_rv_horizontal=(RecyclerView)itemView
                    .findViewById(R.id.item_rv_horizontal);
        }
    }

}
