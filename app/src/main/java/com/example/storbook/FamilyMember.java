package com.example.storbook;

public class FamilyMember {
    String name, relationship, info;

    private String mImageUrl;



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
    }
}
