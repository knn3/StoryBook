package com.example.storbook;

import java.util.ArrayList;
import java.util.List;

public class FamilyMember {
    String name, relationship, info;

    String mImageUrl;

    List<String> relatedMedia;



    public String getUrl (){
        return mImageUrl;
    }

    public void setUrl (String url){
        mImageUrl = url;
    }

    public FamilyMember(String name, String relationship, String info, String imageUrl) {
        this.name = name;
        this.relationship = relationship;
        this.info = info;
        this.mImageUrl = imageUrl;
        this.relatedMedia = null;
    }

    public FamilyMember(String name, String relationship, String info, String imageUrl, List<String> imgUrls) {
        this.name = name;
        this.relationship = relationship;
        this.info = info;
        this.mImageUrl = imageUrl;
        this.relatedMedia = imgUrls;
    }
}
