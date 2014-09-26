package com.learning.dino.criminalintent;

import java.util.UUID;

/**
 * Created by dbulj on 25/09/2014.
 */
public class Crime {
    private UUID mId;
    private String mTitle;

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Crime(){
        mId = UUID.randomUUID();
    }
}
