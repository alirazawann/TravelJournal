package com.example.aliia.traveljournal;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mKey;
    public Upload()
    {
        //empty constructor needed
    }
    public Upload(String name,String imageUrl)
    {
        if(name.trim().equals(""))
        {
            name="no Name";
        }
        mName=name;
        mImageUrl=imageUrl;

    }

    public String getName() {
        return mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }



    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String mKey) {
        this.mKey = mKey;
    }
}
