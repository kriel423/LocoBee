package com.example.locobee;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mTitle;
    private String mDescription;
    private String mQuantity;
    private String mPrice;
    private String mImageUrl;
    private String mKey;

    public Upload(){
        //empty constructor needed
    }

    public Upload(String title, String description, String quantity, String price, String imageUrl){
        if(title.trim().equals(""))
        {
            title = "No Name";
        }
        mTitle = title;
        mDescription = description;
        mQuantity = quantity;
        mPrice = price;
        mImageUrl = imageUrl;
    }

    public String getmTitle()
    {
        return mTitle;
    }

    public void setName(String name)
    {
        mTitle = name;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
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
}
