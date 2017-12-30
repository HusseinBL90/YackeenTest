package com.navways.yackeentest.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by husse on 25/12/2017.
 */

public class TopStories implements Parcelable{
    private String title;
    private String published_date;
    private String imgUrl;
    private byte[] img;
    public TopStories(String title, String published_date, String imgUrl,byte[] img) {
        this.title = title;
        this.published_date = published_date;
        this.imgUrl = imgUrl;
        this.img = img;
    }

    protected TopStories(Parcel in) {
        title = in.readString();
        published_date = in.readString();
        imgUrl = in.readString();
        img = in.createByteArray();
    }

    public static final Creator<TopStories> CREATOR = new Creator<TopStories>() {
        @Override
        public TopStories createFromParcel(Parcel in) {
            return new TopStories(in);
        }

        @Override
        public TopStories[] newArray(int size) {
            return new TopStories[size];
        }
    };

    public String getTitle() {
        return title;
    }
    public String getPublished_date() {
        return published_date;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public byte[] getImg() {
        return img;
    }
    public void setImg(byte[] img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(published_date);
        dest.writeString(imgUrl);
        dest.writeByteArray(img);
    }
}
