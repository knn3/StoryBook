package com.example.storbook;

public class ImageUrls {
    private String mImageUrl;

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
}
