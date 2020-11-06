package com.example.locobee;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class Upload implements Parcelable {
    private String mTitle;
    private String mCategory;
    private String mQuantity;
    private String mPrice;
    private String mImageUrl;
    private String mKey;

    public Upload(){
        //empty constructor needed
    }

    public Upload(String title, String category, String quantity, String price, String imageUrl){
        if(title.trim().equals(""))
        {
            title = "No Name";
        }
        mTitle = title;
        mCategory = category;
        mQuantity = quantity;
        mPrice = price;
        mImageUrl = imageUrl;
    }

    protected Upload(Parcel in) {
        mTitle = in.readString();
        mCategory = in.readString();
        mQuantity = in.readString();
        mPrice = in.readString();
        mImageUrl = in.readString();
        mKey = in.readString();
    }

    public static final Creator<Upload> CREATOR = new Creator<Upload>() {
        @Override
        public Upload createFromParcel(Parcel in) {
            return new Upload(in);
        }

        @Override
        public Upload[] newArray(int size) {
            return new Upload[size];
        }
    };

    public String getmTitle()
    {
        return mTitle;
    }

    public void setName(String name)
    {
        mTitle = name;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(String mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmImageUrl()
    {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        mImageUrl = imageUrl;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }

    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mCategory);
        dest.writeString(mQuantity);
        dest.writeString(mPrice);
        dest.writeString(mImageUrl);
        dest.writeString(mKey);
    }
}
