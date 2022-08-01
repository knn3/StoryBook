package com.example.storbook;
import java.util.ArrayList;
import java.util.List;


public class FriendMember {
    String name, email, info;

    String mImageUrl;

    List<String> relatedMedia;



    public String getUrl (){
        return mImageUrl;
    }

    public void setUrl (String url){
        mImageUrl = url;
    }

    public FriendMember(String name, String email, String info, String imageUrl) {
        this.name = name;
        this.email = email;
        this.info = info;
        this.mImageUrl = imageUrl;
        this.relatedMedia = null;
    }

    public FriendMember(String name, String email, String info, String imageUrl, List<String> imgUrls) {
        this.name = name;
        this.email = email;
        this.info = info;
        this.mImageUrl = imageUrl;
        this.relatedMedia = imgUrls;
    }
}
