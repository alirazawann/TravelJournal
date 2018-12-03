package com.example.aliia.traveljournal;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String lati;
    private String alti;
    private String mKey;
    public Upload()
    {
        mName="";
        mImageUrl="";
        lati="";
        alti="";
    }
    public Upload(String name,String imageUrl,String l,String a)
    {
        if(name.trim().equals(""))
        {
            name="no Name";
        }
        mName=name;
        mImageUrl=imageUrl;

        lati=l;
        alti=a;
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

    public String getLati() {
        return lati;
    }

    public String getAlti() {
        return alti;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public void setAlti(String alti) {
        this.alti = alti;
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
