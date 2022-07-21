package com.example.storbook;

public class ImageUrls {
    private String mImageUrl;
    private String mKey;

    public ImageUrls(){

    }

    public ImageUrls(String imageUrl){
        mImageUrl = imageUrl;
    }

    public String getUrl (){
        return mImageUrl;
    }

    public void setUrl (String url){
        mImageUrl = url;
    }

    public String getKey(){
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }
}
